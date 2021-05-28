package com.module.SFCensorship.Controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bean.jzgl.DTO.*;
import com.bean.jzgl.Source.FunArchiveSFC;
import com.bean.jzgl.Source.FunArchiveSeq;
import com.bean.jzgl.Source.FunCaseInfo;
import com.bean.jzgl.Source.SysUser;
import com.config.annotations.OperLog;
import com.config.session.UserSession;
import com.enums.EnumSoft;
import com.enums.Enums;
import com.factory.BaseFactory;
import com.module.ArchiveManager.Services.ArrangeArchivesService;
import com.module.Interface.Controllers.WebSocketClientUtil;
import com.module.SFCensorship.Services.SFCensorshipService;
import com.module.SystemManagement.Services.UserService;
import com.util.GlobalUtil;
import com.util.StringUtil;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author MrLu
 * @createTime 2020/9/27 11:04
 * @describe 送检相关
 */
@Controller
@RequestMapping("/SFCensorship")
public class SFCensorshipController extends BaseFactory {
    private final String operModul = "SFCensorship";

    private final
    SFCensorshipService sFCensorshipService;
    private final UserService userServiceByRedis;
    private final ArrangeArchivesService arrangeArchivesService;
    private final
    UserSession userSession;

    @Autowired
    public SFCensorshipController(SFCensorshipService sFCensorshipService, @Qualifier("UserServiceByRedis") UserService userServiceByRedis, ArrangeArchivesService arrangeArchivesService, UserSession userSession) {
        this.sFCensorshipService = sFCensorshipService;
        this.userServiceByRedis = userServiceByRedis;
        this.arrangeArchivesService = arrangeArchivesService;
        this.userSession = userSession;
    }

    /**
     * 分页查询送检列表
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2020/9/27 11:11
     */
    @RequestMapping(value = "/selectSFCensorshipPage", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "送检记录分页查询", operType = OperLog.type.SELECT)
    public Map<String, Object> selectSFCensorshipPage(Integer offset, Integer limit, String params) {
        Map<String, Object> reMap = new HashMap<>();
        try {
            //参数列表
            JSONObject pJsonObj = JSON.parseObject(params);
            pJsonObj.put("pageStart", String.valueOf((offset - 1) * limit));
            pJsonObj.put("pageEnd", String.valueOf((offset) * limit));
            //解密id参数
            pJsonObj.put("caseinfoid", DecodeUrlP(pJsonObj.getString("id")));//解密的id
            reMap.put("rows", transformBmField(sFCensorshipService.selectArchiveSFCPage(pJsonObj), FunArchiveSFC.class));
            reMap.put("total", sFCensorshipService.selectArchiveSFCPageCount(pJsonObj));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reMap;
    }

    @RequestMapping(value = "/createNewSFCensorship", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "新建送检记录", operType = OperLog.type.INSERT)
    public String createNewSFCensorship(String params) {
        JSONObject reValue = new JSONObject();
        try {
            //查询案件信息
            JSONObject pJsonObj = JSON.parseObject(params);
            String caseinfoid = pJsonObj.getString("caseinfoid");
            if (StringUtils.isEmpty(caseinfoid)) {
                throw new Exception("关键参数：caseinfoid 未传递");
            }
            int recordId = pJsonObj.getInteger("recordsId");//使用的文书id
            int caseInfoId = Integer.parseInt(DecodeUrlP(caseinfoid));//得到解密后的案件表id
            FunCaseInfo thisFunCaseInfo = sFCensorshipService.getFunCaseInfoById(caseInfoId);
            SysUser userNow = userServiceByRedis.getUserNow(null);//获取当前用户
            //新建送检记录
            FunArchiveSFC newSfc = new FunArchiveSFC();
            newSfc.setJqbh(thisFunCaseInfo.getJqbh());
            newSfc.setAjbh(thisFunCaseInfo.getAjbh());
            newSfc.setIssend(Enums.IsSend.NO);
            newSfc.setAuthor(userNow.getXm());
            newSfc.setAuthoridcard(userNow.getIdcardnumber());
            String sjlxString = pJsonObj.getString("recordscode");
            //recordscode如果为空怎么办呢？   你不会不让他空啊
            EnumSoft.sjlx thisSjlx = EnumUtils.getEnum(EnumSoft.sjlx.class, sjlxString);
            newSfc.setSfcnumber(thisFunCaseInfo.getSfcnumber());//送检编号
            newSfc.setArchivetype(thisSjlx.getValue());//
            newSfc.setArchivename(pJsonObj.getString("archivename"));
            newSfc.setCaseinfoid(thisFunCaseInfo.getId());//案件表id
            newSfc.setBaserecordid(recordId);//基于那张文书选择的
            newSfc.setIssuspectorder(1);//已经排序
            sFCensorshipService.insertFunArchiveSFC(newSfc);
            //查询正在活跃的基础卷
            FunArchiveSeqDTO BaseSeq = sFCensorshipService.selectActiveSeqByCaseId(caseInfoId);
            //新建送检整理次序
//            FunArchiveSeqDTO newSeq = BaseSeq;
            BaseSeq.setArchivesfcid(newSfc.getId());//送检次序id
            BaseSeq.setAuthor(userNow.getXm());//整理人姓名
            BaseSeq.setAuthoridcard(userNow.getIdcardnumber());//整理人身份证号
            BaseSeq.setBatchesseq(0);//新建送检记录 为第0次
            //TODO MrLu 2020/10/4 卷宗编号是干啥的来着？？
            BaseSeq.setArchivetype(newSfc.getArchivetype());//送检类型
            BaseSeq.setArchivename(newSfc.getArchivename());//送检名
            BaseSeq.setBaserecordid(newSfc.getBaserecordid());//基于某张文书生成
            BaseSeq.setPrevid(BaseSeq.getId());
            sFCensorshipService.insertFunArchiveSeq(BaseSeq);
            //查询选择的文书对应的嫌疑人id  这个文书可能是一个对多个人的文书
            List<FunSuspectDTO> suspects = sFCensorshipService.selectSuspectByRecordid(recordId);
            //得到一个id list
            //记录该文书已选择过了
/*
            FunArchiveRecordsDTO uRecord = new FunArchiveRecordsDTO();
            uRecord.setId(recordId);
            uRecord.setPrevid(1);
            sFCensorshipService.updateFunArchiveRecordById(uRecord);*/
            //创建新建卷

            //新的卷就是在复制基础卷
            cloneBaseRecords(BaseSeq, suspects);
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }

    /**
     * 复制卷宗到对应的送检记录
     *
     * @param newSeq   送检记录表
     * @param suspects 送检的嫌疑人列表
     * @return void  |
     * @author MrLu
     * @createTime 2020/10/8 13:55
     */
    private void cloneBaseRecords(final FunArchiveSeqDTO newSeq, List<FunSuspectDTO> suspects) {
        List<FunArchiveTypeDTO> oriArchiveTypes = sFCensorshipService.selectArchiveTypeByJqSeq(newSeq.getPrevid());

        int typeOrder = 1;
        for (FunArchiveTypeDTO thisType :
                oriArchiveTypes) {
            int oriTypeId = thisType.getId();//源id
            thisType.setArchiveseqid(newSeq.getId());//整理次序id
            thisType.setArchivesfcid(newSeq.getArchivesfcid());//送检记录id
            thisType.setIsazxt(1);//非安综系统抽取
            thisType.setDefaultorder(typeOrder++);
            //新建一个
            sFCensorshipService.insertFunArchiveType(thisType);
            //新的id
            int newTypeId = thisType.getId();
            /*新建records*/
            //查询该类型的文书    这里不带任何对嫌疑人单选的文书！
            List<FunArchiveRecordsDTO> cloneRecords = sFCensorshipService.selectRecordsByTypeid(oriTypeId);
            //查询这次送检中的嫌疑人
            //新建封皮和封底
            FunArchiveRecordsDTO cover = new FunArchiveRecordsDTO();
            cover.setJqbh(thisType.getJqbh());
            cover.setAjbh(thisType.getAjbh());
            cover.setBaserecordid(0);//封皮目录封底等不会基于什么生成 而是新建一个
            cover.setThisorder(EnumSoft.fplx.COVER.getOrder());
            cover.setRecordname(EnumSoft.fplx.COVER.getName());
            cover.setArchivetypeid(newTypeId);
            cover.setArchivecode(thisType.getArchivecode());
            cover.setArchivesfcid(newSeq.getArchivesfcid());
            cover.setRecordstyle(0);
            cover.setIsdelete(0);
            cover.setPrevid(0);
            cover.setAuthor(newSeq.getAuthor());//整理人姓名
            cover.setAuthorid(newSeq.getAuthorid());//整理人id
            cover.setIsazxt(1);//封皮、目录、封底 都不是安综原有的东西
            cover.setArchiveseqid(newSeq.getId());
            cover.setRecordscode(EnumSoft.fplx.COVER.getValue());//文件代码
            cover.setRecorduuid(UUID.randomUUID().toString());
            cover.setWjbid(0);
            cover.setWjbm(EnumSoft.fplx.COVER.getValue());
            sFCensorshipService.insertZlRecords(cover, thisType);
            //文书目录
            cover.setThisorder(EnumSoft.fplx.INDEX.getOrder());
            cover.setRecordname(EnumSoft.fplx.INDEX.getName());
            cover.setRecordscode(EnumSoft.fplx.INDEX.getValue());//文件代码
            cover.setRecorduuid(UUID.randomUUID().toString());
            cover.setWjbm(EnumSoft.fplx.INDEX.getValue());
            sFCensorshipService.insertZlRecords(cover, thisType);
            //封底
            cover.setThisorder(EnumSoft.fplx.BACKCOVER.getOrder());
            cover.setRecordname(EnumSoft.fplx.BACKCOVER.getName());
            cover.setRecordscode(EnumSoft.fplx.BACKCOVER.getValue());//文件代码
            cover.setWjbm(EnumSoft.fplx.BACKCOVER.getValue());
            cover.setRecorduuid(UUID.randomUUID().toString());
            sFCensorshipService.insertZlRecords(cover, thisType);

//复制其它的文书
            //在复制被送检的嫌疑人的文书
            if (null != suspects && suspects.size() > 0) {
                //将嫌疑人的id单独提取出来组成个list
                List<Integer> suspectsId = suspects.stream().map(FunSuspectDTO::getId).collect(Collectors.toList());
                List<FunArchiveRecordsDTO> suspectRecord = sFCensorshipService.selectRecordsBySuspects(oriTypeId, suspectsId);
                if (null != suspectRecord && suspectRecord.size() > 0) {
                    cloneRecords.addAll(suspectRecord);
                }
            }
            for (FunArchiveRecordsDTO thisRecord :
                    cloneRecords) {
                if (0 == thisRecord.getRecordstyle()) {
                    //卷首卷尾目录等系统文书不复制
                    continue;
                }
                thisRecord.setArchivesfcid(newSeq.getArchivesfcid());//送检次序id
                thisRecord.setArchiveseqid(newSeq.getId());//整理次序id
                thisRecord.setArchivetypeid(newTypeId);//对应了新的archiveType表id
                thisRecord.setBaserecordid(thisRecord.getId());//基于的基础卷id
                thisRecord.setPrevid(thisRecord.getId());
//                sFCensorshipService.insertFunArchiveRecords(thisRecord, thisType);
                copyRecordsToNew(thisRecord);
            }

        }
    }

    /**
     * 根据案件信息表查询案件信息
     *
     * @param caseinfoid
     * @return String  |
     * @author MrLu
     * @createTime 2020/11/21 11:37
     */
    @RequestMapping(value = "/selectCaseInfoById", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "查询案件信息", operType = OperLog.type.INSERT)
    public String selectCaseInfoById(String caseinfoid) {
        JSONObject reValue = new JSONObject();
        try {
            if (StringUtils.isEmpty(caseinfoid)) {
                throw new Exception("你想查nm吗？");
            }
            int caseInfoId = Integer.parseInt(DecodeUrlP(caseinfoid));

            reValue.put("value", sFCensorshipService.getFunCaseInfoById(caseInfoId));
            FunArchiveSFCDTO sfc = sFCensorshipService.selectBaseSfcByCaseinfoid(caseInfoId);
            //判断该卷是否被整理
            if (null != sfc) {
                //有基础卷
                reValue.put("issuspectorder", 1 == sFCensorshipService.selectBaseSfcByCaseinfoid(caseInfoId).getIssuspectorder());//基础卷是否已经为嫌疑人排序
            } else {
                //没有基础卷
                reValue.put("issuspectorder", false);
            }
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }


    /**
     * 通过案件id查询案件下的所有嫌疑人
     *
     * @param caseinfoid
     * @return String  |
     * @author MrLu
     * @createTime 2020/11/22 18:33
     */
    @RequestMapping(value = "/selectSuspectByCaseInfoId", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "通过caseinfoid查询该案件对应的所有嫌疑人", operType = OperLog.type.SELECT)
    public String selectSuspectByCaseInfoId(String caseinfoid) {
        JSONObject reValue = new JSONObject();
        try {
            if (StringUtils.isEmpty(caseinfoid)) {
                throw new Exception("缺少必要值");
            }
            int caseInfoId = Integer.parseInt(DecodeUrlP(caseinfoid));
            List<FunSuspectDTO> suspects = new ArrayList<>();
            //说明是基础卷
            suspects = sFCensorshipService.selectSuspectById(caseInfoId);
            reValue.put("value", suspects);
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }

    /**
     * 发送打包
     *
     * @param id fun_archivesfc表id
     * @return |
     * @author MrLu
     * @createTime 2021/5/26 15:11
     */
    @RequestMapping(value = "/sendArchive", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "发送打包", operType = OperLog.type.SELECT)
    public String sendArchive(String id) {

        JSONObject reValue = new JSONObject();
        try {
            //解密ip
            int sfcId = Integer.parseInt(DecodeUrlP(id));
            //找到需要打包的seq
            FunArchiveSeq needSeq = arrangeArchivesService.selectLastSeqBySfc(sfcId);
            //调用打包程序
            String version = GlobalUtil.getGlobal("version");//查询的单位代码
            JSONObject paramJsonObj = new JSONObject();//打包所需参数
            //根据不同版本调用不同的打包程序
            if ("province".equals(version)) {
                paramJsonObj.put("key", Enums.passwordSwitch.sendToAz.getValue());
                paramJsonObj.put("message", userSession.getTempString());
                paramJsonObj.put("seqid", needSeq.getId());
            } else {
                paramJsonObj.put("key", Enums.passwordSwitch.sendToZfxz.getValue());
            }
            String archiveUrl = GlobalUtil.getGlobal("archiveUrl");//查询发送打包的websocket地址
            if (StringUtils.isNotEmpty(archiveUrl)) {
                WebSocketClientUtil c = new WebSocketClientUtil(new URI("ws://localhost:9003")); // more about drafts here: http://github.com/TooTallNate/Java-WebSocket/wiki/Drafts

                c.connectBlocking();//等待连接成功ing...
                c.getReadyState();
                c.send(paramJsonObj.toJSONString());//发送参数
            } else {
                throw new Exception("打你妈包呢，地址呢");
            }
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }


    @RequestMapping(value = "/orderArchivesBySuspectOrder", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "通过caseinfoid查询该案件对应的所有嫌疑人", operType = OperLog.type.SELECT)
    public String orderArchivesBySuspectOrder(String issuspectorder, String caseinfoid) {
        JSONObject reValue = new JSONObject();
        try {
            //为嫌疑人整理顺序
            if (StringUtils.isEmpty(issuspectorder) && StringUtils.isEmpty(caseinfoid)) {
                throw new Exception("你传尼玛呢?????????????");
            }
            String[] SuspectOrder = issuspectorder.split(",");
            //案件表id
            int CaseInfoId = Integer.parseInt(DecodeUrlP(caseinfoid));
            //按照顺序循环所有的嫌疑人
            int sOrder = 1;//嫌疑人相对顺序
            FunSuspectDTO funSuspectDTO = new FunSuspectDTO();

            for (String thissuspectid : SuspectOrder) {
                int thisSuspectId = StringUtil.StringToInteger(thissuspectid);
                funSuspectDTO.setId(thisSuspectId);
                funSuspectDTO.setDefaultorder(sOrder++);
                //更新嫌疑人顺序
                sFCensorshipService.updateSuspectDefaultOrder(funSuspectDTO);
            }
            //查询需要更改顺序的seq
            List<FunArchiveSeqDTO> seqList = sFCensorshipService.selectActiveSeqByCaseInfoId(CaseInfoId);
            SysUser userNow = userServiceByRedis.getUserNow(null);//获取当前用户
            for (FunArchiveSeqDTO thisSeq :
                    seqList) {

                //新建送检次序
                thisSeq.setAuthor(userNow.getXm());//创建人姓名
                thisSeq.setAuthorid(userNow.getId());//创建人id
                thisSeq.setAuthoridcard(userNow.getIdcardnumber());//创建人身份证号
                thisSeq.setPrevid(thisSeq.getId());//上一个id
                thisSeq.setBatchesseq(thisSeq.getBatchesseq() + 1);//整理次序
                thisSeq.setIsactive(0);//是否活跃
                // 更新此送检次序为不活跃状态(其实是更新一个sfc下面的所有seq的isactive为1（不活跃）)
                sFCensorshipService.updateBaseSeqIsNotActive(thisSeq.getArchivesfcid());
                sFCensorshipService.insertFunArchiveSeq(thisSeq);//新建seq
                //上一次的id
                int oriSeqId = thisSeq.getPrevid();

                //按照seq的文书类型重新整理
                createArchiveBySuspect(oriSeqId, thisSeq, SuspectOrder);

                websocketSendMessage(thisSeq.getArchivename() + ",已智能排序完毕!"
                        , userNow.getUsername(),
                        Enums.messagetype.typeZero);
                //标记为已为嫌疑人排序
                sFCensorshipService.updateIssuspectorderBySfcId(1, thisSeq.getArchivesfcid());
            }


            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }

    /**
     * FunArchiveSeqDTOMapper
     * 根据嫌疑人顺序自动整理基础卷
     *
     * @param oriSeqId 原有的整理次序id
     * @param newSeq   新建的整理次序id
     * @return |
     * @author MrLu
     * @createTime 2020/12/15 11:06
     */
    private void createArchiveBySuspect(int oriSeqId, FunArchiveSeqDTO newSeq, String[] SuspectOrder) throws Exception {
        //得到基础卷的默认顺序
        List<SysRecordorderDTO> baseOrder = sFCensorshipService.selectSysRecordOrderByArchiveType(newSeq.getArchivetype());
        //查询该卷类型需要的卷
        List<SysRecordtypeorderDTO> needRecordType = sFCensorshipService.selectRecordtypeorderByArchivetype(newSeq.getArchivetype());
        FunArchiveTypeDTO funArchiveTypeDTO = new FunArchiveTypeDTO();
        funArchiveTypeDTO.setArchiveseqid(newSeq.getId());//seqid
        funArchiveTypeDTO.setIsazxt(1);//不是案宗来的了
        funArchiveTypeDTO.setJqbh(newSeq.getJqbh());//警情编号
        funArchiveTypeDTO.setArchivesfcid(newSeq.getArchivesfcid());//sfcid
        funArchiveTypeDTO.setAjbh(newSeq.getAjbh());//案件编号
        funArchiveTypeDTO.setArchivename("?");//不知道干啥的两个字段
        funArchiveTypeDTO.setArchivecode("?");
        //循环插入对应的类型
        Map<Integer, Integer> recordTypeIdMap = new HashMap<>();
        for (SysRecordtypeorderDTO thisRecordType : needRecordType) {
            funArchiveTypeDTO.setRecordtype(thisRecordType.getRecordcode());//文书类型代码
            funArchiveTypeDTO.setRecordtypecn(thisRecordType.getRecordname());//文书类型名
            funArchiveTypeDTO.setDefaultorder(thisRecordType.getDefaultorder());//默认顺序
            sFCensorshipService.insertFunArchiveType(funArchiveTypeDTO);//插入
            //将类型对应的type表的id记录下来
            recordTypeIdMap.put(thisRecordType.getRecordcode(), funArchiveTypeDTO.getId());

        }
        int i = 0;//页数
        Set<Integer> recordType = new HashSet<>();//文书类型 默认没有
        //循环所有的顺序
        for (SysRecordorderDTO thisOrder :
                baseOrder) {
            //判断文书是否对嫌疑人
            if (1 == thisOrder.getRecordstyle() || 7 == thisOrder.getRecordstyle()) {
                //如果对嫌疑人 按照人查询文书
                //卷类型创建过
                if (recordType.contains(thisOrder.getRecordtype())) {
                    continue;
                }
                //循环这个嫌疑人的顺序
                for (String thissuspectid : SuspectOrder) {
                    int thisSuspectId = StringUtil.StringToInteger(thissuspectid);
                    //该嫌疑人在此卷下的所有文书
                    List<FunArchiveRecordsDTO> suspectRecord = sFCensorshipService.selectRecordOrderForSuspect(thisSuspectId, thisOrder.getArchivetype(), thisOrder.getRecordtype(), oriSeqId);
                    //把嫌疑人的文书按顺序插入
                    int j = 1;//相对顺序
                    for (FunArchiveRecordsDTO thisSuspectRecord :
                            suspectRecord) {
                        i = i + j++;
                        thisSuspectRecord.setArchivetypeid(recordTypeIdMap.get(thisOrder.getRecordtype()));//typeid
                        thisSuspectRecord.setArchiveseqid(newSeq.getId());//seqid
                        thisSuspectRecord.setThisorder(i);//顺序
//                        thisSuspectRecord.setArchivesfcid(newSeq.getArchivesfcid());
                        copyRecordsToNew(thisSuspectRecord);//（copy）
                    /*    FunSuspectRecordDTO sr = sFCensorshipService.selectSuspectRecordByRid(thisSuspectRecord.getId());//此时使用原有的id查询
                        if (null != sr) {
                            //这里应该保证sr不能为空  因为这个文书如果对人但是在嫌疑人文书关联表中没有数据那么就是数据出现错误了  是个问题了
                            //对人文书复制关联表
                            sr.setRecordid(thisSuspectRecord.getId());//注意此时是新的id了
                            sr.setArchiveseqid(newSeq.getId());//新的seqid
                            sFCensorshipService.insertSuspectRecord(sr);
                        }*/
                    }
                }
                recordType.add(thisOrder.getRecordtype());//记录当前操作的文书类型
            } else {
                //文书不是对嫌疑人的
                //查看这个顺序的应有的文书
                List<FunArchiveRecordsDTO> thisRecord = sFCensorshipService.selectReocrdBySeqRcode(oriSeqId, thisOrder.getRecordcode(), thisOrder.getRecordtype());
                //同一个文书代码可能对应多个文书（大多数情况就一个）
                int j = 1;//相对顺序
                for (FunArchiveRecordsDTO thisReocrd : thisRecord) {
                    i += j++;
                    thisReocrd.setArchivetypeid(recordTypeIdMap.get(thisOrder.getRecordtype()));//typeid
                    thisReocrd.setArchiveseqid(newSeq.getId());//seqid
                    thisReocrd.setThisorder(i);//顺序
                    copyRecordsToNew(thisReocrd);//插入（copy）
                }
            }

        }
    }

    private void copyRecordsToNew(FunArchiveRecordsDTO newRecord) {
        int oriId = newRecord.getId();//此时还是原有的id
        newRecord.setPrevid(oriId);//上一个 源于谁的id
        sFCensorshipService.insertFunArchiveRecords(newRecord);//新建 （此时该实体类的id已经变成新的了）
        copyFilesToNew(oriId, newRecord);
        //判断是否是对人文书 如果是对人文书则复制关系表
        if (1 == newRecord.getRecordstyle() || 7 == newRecord.getRecordstyle()) {
            FunSuspectRecordDTO sr = sFCensorshipService.selectSuspectRecordByRid(newRecord.getPrevid());//此时使用原有的id查询
            if (null != sr) {
                //这里应该保证sr不能为空  因为这个文书如果对人但是在嫌疑人文书关联表中没有数据那么就是数据出现错误了  是个问题了
                //对人文书复制关联表
                sr.setRecordid(newRecord.getId());//注意此时是新的id了
                sr.setArchiveseqid(newRecord.getArchiveseqid());//新的seqid
                sFCensorshipService.insertSuspectRecord(sr);
            }
        }

        //复制文书也要复制对应的标签
        //查找这个文书的标签
        for (FunArchiveTagsDTO thisTag :
                sFCensorshipService.selectRecordByRecordId(oriId)) {
            //开始复制了呢
            thisTag.setRecordid(newRecord.getId());
            thisTag.setArchiveseqid(newRecord.getArchiveseqid());
            sFCensorshipService.createNewTags(thisTag);//新建过去
        }
    }

    private void copyFilesToNew(int oriId, FunArchiveRecordsDTO newRecord) {

        int newRecordId = newRecord.getId();//新建的文书id
        //开始复制文书文件
        //查找该文书原有的文件
        for (FunArchiveFilesDTO thisFile :
                sFCensorshipService.selectRecordFilesByRecordId(oriId, null)) {
            //判断该文件的uuid是否有重复  如果有的话重新生成uuid  同一个seq下不允许有重复的filecode ！
            int Repeated = sFCensorshipService.selectRepeatedlyFileCodeBySeqid(thisFile.getFilecode(), newRecord.getArchiveseqid());
            if (Repeated > 0) {
                thisFile.setFilecode(UUID.randomUUID().toString());
            }

            thisFile.setArchiveseqid(newRecord.getArchiveseqid());
            thisFile.setArchivesfcid(newRecord.getArchivesfcid());
            thisFile.setArchivetypeid(newRecord.getArchivetypeid());
            thisFile.setArchiverecordid(newRecordId);
            thisFile.setAuthor(newRecord.getAuthor());
            thisFile.setAuthorid(newRecord.getAuthorid());

            //复制插入
            sFCensorshipService.insertFunArchiveFilesDTO(thisFile);
        }

    }

}
