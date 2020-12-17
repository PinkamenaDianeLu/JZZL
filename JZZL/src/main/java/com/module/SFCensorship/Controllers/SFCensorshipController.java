package com.module.SFCensorship.Controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bean.jzgl.DTO.*;
import com.bean.jzgl.Source.FunArchiveSFC;
import com.bean.jzgl.Source.FunArchiveSeq;
import com.bean.jzgl.Source.FunCaseInfo;
import com.bean.jzgl.Source.SysUser;
import com.config.annotations.OperLog;
import com.enums.EnumSoft;
import com.enums.Enums;
import com.factory.BaseFactory;
import com.module.SFCensorship.Services.SFCensorshipService;
import com.module.SystemManagement.Services.UserService;
import com.util.StringUtil;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

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

    @Autowired
    public SFCensorshipController(SFCensorshipService sFCensorshipService, @Qualifier("UserServiceByRedis") UserService userServiceByRedis) {
        this.sFCensorshipService = sFCensorshipService;
        this.userServiceByRedis = userServiceByRedis;
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
//            newSfc.setPeoplecaseid(peopelcaseid);
            String sjlxString = pJsonObj.getString("recordscode");
            //recordscode如果为空怎么办呢？   你不会不让他空啊
            EnumSoft.sjlx thisSjlx = EnumUtils.getEnum(EnumSoft.sjlx.class, sjlxString);
            newSfc.setSfcnumber(thisFunCaseInfo.getSfcnumber());//送检编号
            newSfc.setArchivetype(thisSjlx.getValue());//
            newSfc.setArchivename(pJsonObj.getString("archivename"));
            newSfc.setCaseinfoid(thisFunCaseInfo.getId());//案件表id
            newSfc.setBaserecordid(recordId);//基于那张文书选择的
            sFCensorshipService.insertFunArchiveSFC(newSfc);
            //新建送检整理次序
            FunArchiveSeq newSeq = new FunArchiveSeq();
            newSeq.setArchivesfcid(newSfc.getId());//送检次序id
            newSeq.setJqbh(newSfc.getJqbh());
            newSeq.setAjbh(newSfc.getAjbh());
            newSeq.setAuthor(userNow.getXm());//整理人姓名
            newSeq.setAuthoridcard(userNow.getIdcardnumber());//整理人身份证号
            newSeq.setBatchesseq(0);//新建送检记录 为第0次
            //TODO MrLu 2020/10/4 卷宗编号是干啥的来着？？
            newSeq.setCaseinfoid(newSfc.getCaseinfoid());//案件表id
            newSeq.setIsfinal(Enums.IsFinal.NO);//是否是完结版
            newSeq.setSfcnumber(newSfc.getSfcnumber());//送检编号
            newSeq.setArchivetype(newSfc.getArchivetype());//送检类型
            newSeq.setArchivename(newSfc.getArchivename());//送检名
            newSeq.setBaserecordid(newSfc.getBaserecordid());//基于某张文书生成
            sFCensorshipService.insertFunArchiveSeq(newSeq);

            //记录该文书已选择过了

            FunArchiveRecordsDTO uRecord = new FunArchiveRecordsDTO();
            uRecord.setId(recordId);
            uRecord.setPrevid(1);
            sFCensorshipService.updateFunArchiveRecordById(uRecord);
            //创建新建卷
//            cloneRecords(thisFunPeopelCase.getJqbh(), newSeq);
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
     * @param jqbh   警情编号
     * @param newSeq 送检记录表
     * @return void  |
     * @author MrLu
     * @createTime 2020/10/8 13:55
     */
    private void cloneRecords(String jqbh, final FunArchiveSeq newSeq) {
        List<FunArchiveTypeDTO> oriArchiveTypes = sFCensorshipService.selectArchiveTypeByJqSeq(jqbh, 0);
        for (FunArchiveTypeDTO thisOriAt :
                oriArchiveTypes) {
            int oriTypeId = thisOriAt.getId();//源id
            thisOriAt.setArchiveseqid(newSeq.getId());//整理次序id
            thisOriAt.setArchivesfcid(newSeq.getArchivesfcid());//送检记录id
            thisOriAt.setIsazxt(1);//非安综系统抽取
            //新建一个
            sFCensorshipService.insertFunArchiveType(thisOriAt);
            //新的id
            int newTypeId = thisOriAt.getId();
            /*新建records*/
            Map<String, Object> precordMap = new HashMap<>();//查询参数
            precordMap.put("jqbh", jqbh);
            precordMap.put("archivetypeid", oriTypeId);
            //查询该类型的文书
            List<FunArchiveRecordsDTO> cloneRecords = sFCensorshipService.selectRecordsByJqbh(precordMap);
            //新建封皮和封底
            FunArchiveRecordsDTO cover = new FunArchiveRecordsDTO();
            cover.setJqbh(thisOriAt.getJqbh());
            cover.setAjbh(thisOriAt.getAjbh());
            cover.setThisorder(EnumSoft.fplx.COVER.getOrder());
            cover.setRecordname(EnumSoft.fplx.COVER.getName());
            cover.setArchivetypeid(newTypeId);
            cover.setArchivecode(thisOriAt.getArchivecode());
            cover.setArchivesfcid(newSeq.getArchivesfcid());
            cover.setRecordstyle(0);
            cover.setIsdelete(0);
            cover.setPrevid(0);
            cover.setAuthor(newSeq.getAuthor());//整理人姓名
            cover.setAuthorid(newSeq.getAuthorid());//整理人id
            cover.setIsazxt(1);//封皮、目录、封底 都不是安综原有的东西
            cover.setArchiveseqid(newSeq.getId());
            cover.setRecordscode(EnumSoft.fplx.COVER.getValue());//文件代码
            sFCensorshipService.insertFunArchiveRecords(cover, thisOriAt);
            //文书目录
            cover.setThisorder(EnumSoft.fplx.INDEX.getOrder());
            cover.setRecordname(EnumSoft.fplx.INDEX.getName());
            cover.setRecordscode(EnumSoft.fplx.INDEX.getValue());//文件代码
            sFCensorshipService.insertFunArchiveRecords(cover, thisOriAt);
            //封底
            cover.setThisorder(EnumSoft.fplx.BACKCOVER.getOrder());
            cover.setRecordname(EnumSoft.fplx.BACKCOVER.getName());
            cover.setRecordscode(EnumSoft.fplx.BACKCOVER.getValue());//文件代码
            sFCensorshipService.insertFunArchiveRecords(cover, thisOriAt);


            for (FunArchiveRecordsDTO thisRecord :
                    cloneRecords) {
                thisRecord.setArchivesfcid(newSeq.getArchivesfcid());//送检次序id
                thisRecord.setArchiveseqid(newSeq.getId());//整理次序id
                thisRecord.setArchivetypeid(newTypeId);//对应了新的archiveType表id
                //这里不要改变它的所属人和id 应为这是他们还是安综抽过来的 文件没有任何变化
                sFCensorshipService.insertFunArchiveRecords(thisRecord, thisOriAt);
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
            int caseInfoId=Integer.parseInt(DecodeUrlP(caseinfoid));

            reValue.put("value", sFCensorshipService.getFunCaseInfoById(caseInfoId));
            reValue.put("issuspectorder", 1==sFCensorshipService.selectBaseSfcByCaseinfoid(caseInfoId).getIssuspectorder());//基础卷是否已经为嫌疑人排序
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
            List<FunArchiveSeqDTO> seqList = sFCensorshipService.selectActiveSeqByCaseInfoId(CaseInfoId);
            SysUser userNow = userServiceByRedis.getUserNow(null);//获取当前用户
            for (FunArchiveSeqDTO thisSeq :
                    seqList) {
                // 更新此送检次序为不活跃状态(其实是更新一个sfc下面的所有seq的isactive为1（不活跃）)
                sFCensorshipService.updateBaseSeqIsNotActive(thisSeq.getArchivesfcid());
                //新建送检次序
                thisSeq.setAuthor(userNow.getXm());//创建人姓名
                thisSeq.setAuthorid(userNow.getId());//创建人id
                thisSeq.setAuthoridcard(userNow.getIdcardnumber());//创建人身份证号
                thisSeq.setPrevid(thisSeq.getId());//上一个id
                thisSeq.setBatchesseq(thisSeq.getBatchesseq() + 1);//整理次序
                thisSeq.setIsactive(0);//是否活跃
                sFCensorshipService.insertFunArchiveSeq(thisSeq);
                //上一次的id
                int oriSeqId = thisSeq.getPrevid();

                //按照seq的文书类型重新整理
               createArchiveBySuspect(oriSeqId, thisSeq);
            }
            //标记为已为嫌疑人排序
            sFCensorshipService.updateIssuspectorderByCaseinfoid(CaseInfoId);
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }

    /**FunArchiveSeqDTOMapper
     * 根据嫌疑人顺序自动整理基础卷
     *
     * @param oriSeqId 原有的整理次序id
     * @param newSeq   新建的整理次序id
     * @return |
     * @author MrLu
     * @createTime 2020/12/15 11:06
     */
    private void createArchiveBySuspect(int oriSeqId, FunArchiveSeqDTO newSeq) throws Exception {
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
//            funArchiveTypeDTO.setr
            funArchiveTypeDTO.setRecordtype(thisRecordType.getRecordcode());//文书类型代码
            funArchiveTypeDTO.setRecordtypecn(thisRecordType.getRecordname());//文书类型名
            funArchiveTypeDTO.setDefaultorder(thisRecordType.getDefaultorder());//默认顺序
            sFCensorshipService.insertFunArchiveType(funArchiveTypeDTO);//插入
            //将类型对应的type表的id记录下来
            recordTypeIdMap.put(thisRecordType.getRecordcode(), funArchiveTypeDTO.getId());

        }
        System.out.println(recordTypeIdMap);
        int i = 0;
        int test =0;
        for (SysRecordorderDTO thisOrder :
                baseOrder) {
            //查看这个顺序的应有的文书
            List<FunArchiveRecordsDTO> thisRecord = sFCensorshipService.selectReocrdBySeqRcode(oriSeqId, thisOrder.getRecordcode(),thisOrder.getRecordtype());
            //同一个文书代码可能对应多个文书（大多数情况就一个）
            int j = 0;
            for (FunArchiveRecordsDTO thisReocrd : thisRecord) {
                test++;
                i += j;
                thisReocrd.setArchivetypeid(recordTypeIdMap.get(thisOrder.getRecordtype()));//typeid
                thisReocrd.setArchiveseqid(newSeq.getId());//seqid
                thisReocrd.setThisorder(thisOrder.getDefaultorder() + i++);//顺序
                thisReocrd.setPrevid(thisReocrd.getId());//上一个
                copyRecordToNew(thisReocrd.getId(), thisReocrd);//复制文书下面的图片
            }
        }
        System.out.println(i);
        System.out.println("新建文书："+test);
    }

    private void copyRecordToNew(int oriId, FunArchiveRecordsDTO newRecord) {
        sFCensorshipService.insertFunArchiveRecords(newRecord);//新建
        int newRecordId = newRecord.getId();//新建的文书id
        //开始复制文书文件
        //查找该文书原有的文件
        for (FunArchiveFilesDTO thisFile :
                sFCensorshipService.selectRecordFilesByRecordId(oriId, null)) {
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
