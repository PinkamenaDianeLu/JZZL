package com.module.CaseManager.Controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bean.jzgl.DTO.*;
import com.bean.jzgl.Source.SysUser;
import com.config.annotations.OperLog;
import com.enums.EnumSoft;
import com.enums.Enums;
import com.factory.BaseFactory;
import com.module.CaseManager.Services.CaseManagerService;
import com.module.SystemManagement.Services.UserService;
import com.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author MrLu
 * @createTime 2021/1/8 15:17
 * @describe 案件管理  合案、拆案
 */
@Controller
@RequestMapping("/CaseManager")
public class CaseManagerController extends BaseFactory {
    private final String operModul = "CaseManager";

    private final UserService userServiceByRedis;
    private final
    CaseManagerService caseManagerService;

    @Autowired
    public CaseManagerController(@Qualifier("UserServiceByRedis") UserService userServiceByRedis, CaseManagerService caseManagerService) {
        this.userServiceByRedis = userServiceByRedis;
        this.caseManagerService = caseManagerService;
    }


    /**
     * 查询案件信息
     *
     * @param
     * @return |
     * @author Mrlu
     * @createTime 2021/3/4 15:15
     */
    @RequestMapping(value = "/selectCaseMessage", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "查询案件的信息", operType = OperLog.type.SELECT)
    public String selectCaseMessage(Integer caseinfoid) {
        JSONObject reValue = new JSONObject();
        try {
            FunCaseInfoDTO caseInfo = caseManagerService.selectCaseInfoById(caseinfoid);
            reValue.put("value", caseInfo);
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }

    /**
     * 根据案件id查询嫌疑人
     *
     * @param
     * @return |
     * @author Mrlu
     * @createTime 2021/3/4 15:54
     */
    @RequestMapping(value = "/selectSuspectByCaseId", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "根据案件id查询嫌疑人", operType = OperLog.type.SELECT)
    public String selectSuspectByCaseId(Integer caseinfoid) {
        JSONObject reValue = new JSONObject();
        try {
            reValue.put("value", caseManagerService.selectSuspectByCaseinfoId(caseinfoid));
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }


    @RequestMapping(value = "/selectPeopleCaseForCombinationPage", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "查询可用于合案的案件", operType = OperLog.type.SELECT)
    public Map<String, Object> selectPeopleCaseForCombinationPage(Integer offset, Integer limit, String params) {
        Map<String, Object> reMap = new HashMap<>();
        try {
            //参数列表
            JSONObject pJsonObj = JSON.parseObject(params);
            pJsonObj.put("pageStart", String.valueOf((offset - 1) * limit));
            pJsonObj.put("pageEnd", String.valueOf((offset) * limit));
            SysUser userNow = userServiceByRedis.getUserNow(null);//获取当前用户
            pJsonObj.put("sysuserid", userNow.getId());
            reMap.put("rows", caseManagerService.selectPeopleCaseForCombinationPage(pJsonObj));
            reMap.put("total", caseManagerService.selectPeopleCaseForCombinationCount(pJsonObj));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reMap;
    }

    @RequestMapping(value = "/selectSuspectByCaseIds", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "查询多个案件id下的嫌疑人", operType = OperLog.type.SELECT)
    public String selectSuspectByCaseIds(Integer maincaseinfoid, String appurtenances) {
        JSONObject reValue = new JSONObject();
        try {
            JSONArray suspects = new JSONArray();
            String[] appurtenanceIds = Optional.ofNullable(appurtenances).orElse("").split(",");
            for (String thisCaseId :
                    appurtenanceIds) {
                JSONObject caseInfoJsonObj = new JSONObject();
                int caseId = Integer.parseInt(thisCaseId);
                FunCaseInfoDTO caseInfo = caseManagerService.selectCaseInfoById(caseId);//案件信息
                List<FunSuspectDTO> suspectsForCase = caseManagerService.selectSuspectByCaseinfoId(caseId);//案件的嫌疑人们
                caseInfoJsonObj.put("caseinfo", caseInfo);
                caseInfoJsonObj.put("suspects", suspectsForCase);
                suspects.add(caseInfoJsonObj);
            }
            reValue.put("mainSuspect", caseManagerService.selectSuspectByCaseinfoId(maincaseinfoid));//主案嫌疑人们
            reValue.put("appurtenanceSuspect", suspects);//辅案嫌疑人和信息
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }

    /**
     * 合案
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/8 16:45
     */
    @RequestMapping(value = "/combinationCase", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "合案", operType = OperLog.type.INSERT)
    public String combinationCase(Integer mainCaseId, String suspectids, String fcaseids, String newcasename) {
        JSONObject reValue = new JSONObject();
        try {
            FunCaseInfoDTO mainCaseInfo = caseManagerService.selectCaseInfoById(mainCaseId);
            SysUser userNow = userServiceByRedis.getUserNow(null);//获取当前用户
            String dwdm = userNow.getAgencycode().substring(0, 6);
            mainCaseInfo.setJqbh(StringUtil.createJqbh(dwdm));
            mainCaseInfo.setAjbh(StringUtil.createAjbh(dwdm));
            mainCaseInfo.setCasename(newcasename);
            mainCaseInfo.setSfcnumber(StringUtil.createSfcNumber(dwdm.substring(0, 6)));//送检编号
            mainCaseInfo.setCasestate(Enums.CaseState.MERGE.getValue());//合案
            mainCaseInfo.setIssorted(1);//已排序
            caseManagerService.insertCaseinfo(mainCaseInfo);//新建案件信息
            List<FunCasePeoplecaseDTO> casePeople = caseManagerService.selectRelationByCaseid(mainCaseId);//查询原有案件的人关系
            //循环复制关系
            for (FunCasePeoplecaseDTO thisGx :
                    casePeople) {
                thisGx.setAjbh(mainCaseInfo.getAjbh());
                thisGx.setCaseinfoid(mainCaseInfo.getId());
                thisGx.setCasename(mainCaseInfo.getCasename());
                thisGx.setSfcnumber(mainCaseInfo.getSfcnumber());
                thisGx.setJqbh(mainCaseInfo.getJqbh());
                caseManagerService.insertCasePeopleCase(thisGx);
            }
            //查询原案件的sfc以供复制
            FunArchiveSFCDTO oriSfc = caseManagerService.selectBaseSfcByCaseinfoid(mainCaseId, null);
            oriSfc.setSfcnumber(mainCaseInfo.getSfcnumber());
            oriSfc.setAjbh(mainCaseInfo.getAjbh());
            oriSfc.setJqbh(mainCaseInfo.getJqbh());
            oriSfc.setAuthor(userNow.getXm());//创建人
            oriSfc.setAuthoridcard(userNow.getIdcardnumber());//创建人身份证号
            oriSfc.setCaseinfoid(mainCaseInfo.getId());//案件表id
            //新建一个
            caseManagerService.createNewSfc(oriSfc);
            //新建基础卷
            FunArchiveSeqDTO oriSeq = caseManagerService.selectActiveSeqByCaseId(mainCaseId);//查询原始的基础卷的seq
            //查询所有辅案的基础卷
            String[] fCaseIds = Optional.ofNullable(fcaseids).orElse("").split(",");//所有基础卷的id
            List<FunArchiveSeqDTO> FcaseSeq = caseManagerService.selectSuspectsByCaseIds(fCaseIds);
            FcaseSeq.add(oriSeq);//把主案的基础卷也放进去
            Integer OriSeqId = oriSeq.getId();//原始卷的seqid
            oriSeq.setAuthor(oriSfc.getAuthor());
            oriSeq.setAuthoridcard(oriSfc.getAuthoridcard());
            oriSeq.setAuthorid(userNow.getId());
            oriSeq.setBatchesseq(0);//基础卷
            oriSeq.setIsfinal(0);//已经完结了 -> 没有
            oriSeq.setJqbh(oriSfc.getJqbh());//警情编号
            oriSeq.setAjbh(oriSfc.getAjbh());//案件编号
            oriSeq.setCaseinfoid(oriSfc.getCaseinfoid());//案件表id
            oriSeq.setArchivetype(oriSfc.getArchivetype());//基础卷了！
            oriSeq.setArchivename(oriSfc.getArchivename());
            oriSeq.setArchivesfcid(oriSfc.getId());
            oriSeq.setIsactive(0);//正在被使用的
            caseManagerService.createNewSeq(oriSeq);//新建的基础卷


            //通过这些seq找到他们的type  记录是否有不同类型

            List<FunArchiveTypeDTO> archiveTypes = caseManagerService.selectArchiveTypeByJqSeq(OriSeqId);
            List<Integer> seqIdList = FcaseSeq.stream().map(FunArchiveSeqDTO::getId).collect(Collectors.toList());
            seqIdList.add(OriSeqId);//此时seq对象内存中的id已经变成新id了  将原有id放回来
            String[] suspectIds = Optional.ofNullable(suspectids).orElse("").split(",");//所有基础卷的id
            List<FunSuspectDTO> suspectList = caseManagerService.selectSuspectsByIds(suspectIds);
            //存储嫌疑人信息  key：嫌疑人原id  V：复制的嫌疑人对象
            Map<Integer, FunSuspectDTO> suspectMap = new HashMap<>();
            for (FunSuspectDTO thisSuspect :
                    suspectList) {
                int oriSuspectId = thisSuspect.getId();
                thisSuspect.setAjbh(oriSeq.getAjbh());
                thisSuspect.setCaseinfoid(mainCaseInfo.getId());
                thisSuspect.setSfcnumber(oriSeq.getSfcnumber());
                thisSuspect.setJqbh(oriSeq.getJqbh());
                //新建这个嫌疑人
                caseManagerService.insertNewSuspect(thisSuspect);
                suspectMap.put(oriSuspectId, thisSuspect);
            }

            int typeOrder = 1;//type的顺序
            for (FunArchiveTypeDTO thisType :
                    archiveTypes) {
                //查询这个
                List<FunArchiveRecordsDTO> recordsDTOS = caseManagerService.selectRecordsBySeqType(seqIdList, thisType.getRecordtype());
                //新建type
                //复制type
                thisType.setArchiveseqid(oriSeq.getId());
                thisType.setArchivesfcid(oriSeq.getArchivesfcid());
                thisType.setJqbh(oriSeq.getJqbh());
                thisType.setAjbh(oriSeq.getAjbh());
                thisType.setDefaultorder(typeOrder++);//顺序
                caseManagerService.createNewType(thisType);//insert
                //创建封皮、封底
                //新建封皮和封底
                FunArchiveRecordsDTO cover = new FunArchiveRecordsDTO();
                cover.setJqbh(thisType.getJqbh());
                cover.setAjbh(thisType.getAjbh());
                cover.setBaserecordid(0);//封皮目录封底等不会基于什么生成 而是新建一个
                cover.setThisorder(EnumSoft.fplx.COVER.getOrder());
                cover.setRecordname(EnumSoft.fplx.COVER.getName());
                cover.setArchivetypeid(thisType.getId());
                cover.setArchivecode(thisType.getArchivecode());
                cover.setArchivesfcid(oriSeq.getArchivesfcid());
                cover.setRecordstyle(0);
                cover.setIsdelete(0);
                cover.setPrevid(0);
                cover.setAuthor(userNow.getXm());//整理人姓名
                cover.setAuthoridcard(userNow.getIdcardnumber());//整理人身份证号
                cover.setAuthorid(userNow.getId());//整理人id
                cover.setIsazxt(1);//封皮、目录、封底 都不是安综原有的东西
                cover.setArchiveseqid(oriSeq.getId());
                cover.setRecordscode(EnumSoft.fplx.COVER.getValue());//文件代码
                caseManagerService.insertZlRecords(cover, thisType);
                //文书目录
                cover.setThisorder(EnumSoft.fplx.INDEX.getOrder());
                cover.setRecordname(EnumSoft.fplx.INDEX.getName());
                cover.setRecordscode(EnumSoft.fplx.INDEX.getValue());//文件代码
                caseManagerService.insertZlRecords(cover, thisType);
                //封底
                cover.setThisorder(EnumSoft.fplx.BACKCOVER.getOrder());
                cover.setRecordname(EnumSoft.fplx.BACKCOVER.getName());
                cover.setRecordscode(EnumSoft.fplx.BACKCOVER.getValue());//文件代码
                caseManagerService.insertZlRecords(cover, thisType);

                //判断这个文书是否是对选中的嫌疑人
                int i = 3;
                for (FunArchiveRecordsDTO thisRecord :
                        recordsDTOS) {
                    int oriRecordId = thisRecord.getId();
                    //判断文书是否对人
                    if (thisRecord.getRecordstyle() == 1 || thisRecord.getRecordstyle() == 7) {
                        //对人的  判断是否是对选中的嫌疑人
                        if (suspectIds.length == 0) {
                            //没选任何嫌疑人  那么跟嫌疑人有关的文书就都不要
                            continue;
                        } else {
                            FunSuspectRecordDTO sr = caseManagerService.selectRecordBoolSuspect(oriRecordId, suspectIds);
                            if (null != sr) {
                                //证明这个文书是关联与被选中的嫌疑人的
                                thisRecord.setThisorder(i++);
                                thisRecord.setPrevid(oriRecordId);
                                thisRecord.setJqbh(thisType.getJqbh());
                                thisRecord.setAjbh(thisType.getAjbh());
                                thisRecord.setAuthorid(oriSeq.getAuthorid());
                                thisRecord.setAuthor(oriSeq.getAuthor());
                                thisRecord.setArchivetypeid(thisType.getId());//type
                                thisRecord.setArchiveseqid(thisType.getArchiveseqid());//seq
                                thisRecord.setArchivesfcid(thisType.getArchivesfcid());//sfc
                                caseManagerService.createNewRecord(thisRecord);//在这里新建的
                                copyFiles(caseManagerService.selectRecordFilesByRecordId(oriRecordId), thisRecord);

                                //复制关系
                                FunSuspectDTO suspect = suspectMap.get(Optional.ofNullable(sr.getSuspectid()).orElse(0));
                                sr.setArchiveseqid(thisRecord.getArchiveseqid());
                                sr.setRecordid(thisRecord.getId());//新的文书id
                                sr.setJqbh(thisRecord.getJqbh());
                                sr.setAjbh(thisRecord.getAjbh());
                                sr.setSuspectid(suspect.getId());
                                sr.setCaseinfoid(oriSeq.getCaseinfoid());
                                sr.setSfcnumber(oriSeq.getSfcnumber());
                                sr.setRecordtype(thisType.getRecordtype());
                                caseManagerService.createNewSR(sr);//复制关系
                                //找到被复制的嫌疑人
                            }
                        }
                    } else {
                        //不对人  直接复制
                        thisRecord.setThisorder(i++);
                        thisRecord.setPrevid(oriRecordId);
                        thisRecord.setJqbh(thisType.getJqbh());
                        thisRecord.setAjbh(thisType.getAjbh());
                        thisRecord.setAuthorid(oriSeq.getAuthorid());
                        thisRecord.setAuthor(oriSeq.getAuthor());
                        thisRecord.setArchivetypeid(thisType.getId());//type
                        thisRecord.setArchiveseqid(thisType.getArchiveseqid());//seq
                        thisRecord.setArchivesfcid(thisType.getArchivesfcid());//sfc
                        caseManagerService.createNewRecord(thisRecord);//在这里新建的
                        copyFiles(caseManagerService.selectRecordFilesByRecordId(oriRecordId), thisRecord);
                    }
                }
            }

            websocketSendMessage(newcasename + ",已合案完毕!"
                    , userNow.getUsername(),
                    Enums.messagetype.typeSix);
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }


    /**
     * 拆案
     *
     * @param oricaseid     案件id
     * @param suspectids    被选择的嫌疑人们
     * @param notsuspectids 不被选择的嫌疑人们
     * @param oricaseid     案件id
     * @return |
     * @author MrLu
     * @createTime 2021/1/8 15:45
     */
    @RequestMapping(value = "/splitCase", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "拆案", operType = OperLog.type.INSERT)
    public String splitCase(Integer oricaseid, String suspectids, String notsuspectids, String newcasename) {
        JSONObject reValue = new JSONObject();
        try {

            FunCaseInfoDTO oriCaseInfo = caseManagerService.selectCaseInfoById(oricaseid);
            SysUser userNow = userServiceByRedis.getUserNow(null);//获取当前用户
            String dwdm = userNow.getAgencycode().substring(0, 6);
            oriCaseInfo.setJqbh(StringUtil.createJqbh(dwdm));
            oriCaseInfo.setAjbh(StringUtil.createAjbh(dwdm));
            oriCaseInfo.setCasename(Optional.ofNullable(newcasename).orElse(oriCaseInfo.getCasename() + "_拆案"));
            oriCaseInfo.setSfcnumber(StringUtil.createSfcNumber(dwdm.substring(0, 6)));//送检编号
            oriCaseInfo.setCasestate(Enums.CaseState.SPLIT.getValue());//拆案
            oriCaseInfo.setIssorted(1);//已排序
//            oriCaseInfo.setGajgmc("公安机关名称");
//            oriCaseInfo.setGajgdm("公安机关代码");
            //新建案件
            caseManagerService.insertCaseinfo(oriCaseInfo);
            //新建sfc、seq、type
            //案件人员关系表
            //查询原有的主、辅办人
            List<FunCasePeoplecaseDTO> casePeople = caseManagerService.selectRelationByCaseid(oricaseid);
            //循环复制关系
            for (FunCasePeoplecaseDTO thisGx :
                    casePeople) {
                thisGx.setAjbh(oriCaseInfo.getAjbh());
                thisGx.setCaseinfoid(oriCaseInfo.getId());
                thisGx.setCasename(oriCaseInfo.getCasename());
                thisGx.setSfcnumber(oriCaseInfo.getSfcnumber());
                thisGx.setJqbh(oriCaseInfo.getJqbh());
                caseManagerService.insertCasePeopleCase(thisGx);
            }

            FunArchiveSFCDTO oriSfc = caseManagerService.selectBaseSfcByCaseinfoid(oricaseid, null);
            oriSfc.setSfcnumber(oriCaseInfo.getSfcnumber());
            oriSfc.setAjbh(oriCaseInfo.getAjbh());
            oriSfc.setJqbh(oriCaseInfo.getJqbh());
            oriSfc.setAuthor(userNow.getXm());//创建人
            oriSfc.setAuthoridcard(userNow.getIdcardnumber());//创建人身份证号
            oriSfc.setCaseinfoid(oriCaseInfo.getId());//案件表id
            //新建一个
            caseManagerService.createNewSfc(oriSfc);
            //新建基础卷
            FunArchiveSeqDTO oriSeq = caseManagerService.selectActiveSeqByCaseId(oricaseid);//查询原始的基础卷的seq
            Integer OriSeqId = oriSeq.getId();//原始卷的seqid
            oriSeq.setAuthor(oriSfc.getAuthor());
            oriSeq.setCaseinfoid(oriSfc.getCaseinfoid());//案件id
            oriSeq.setBatchesseq(0);//基础卷
            oriSeq.setIsfinal(0);//已经完结了
            oriSeq.setJqbh(oriSfc.getJqbh());//警情编号
            oriSeq.setAjbh(oriSfc.getAjbh());//案件编号
            oriSeq.setArchivetype(oriSfc.getArchivetype());//基础卷了！
            oriSeq.setArchivename(oriSfc.getArchivename());
            oriSeq.setArchivesfcid(oriSfc.getId());
            oriSeq.setIsactive(0);//正在被使用的
            caseManagerService.createNewSeq(oriSeq);//新建的基础卷
            //复制选定的嫌疑人
            String[] SuspectIds = Optional.ofNullable(suspectids).orElse("").split(",");

            List<FunSuspectDTO> suspectList = caseManagerService.selectSuspectsByIds(SuspectIds);
            //存储嫌疑人信息  key：嫌疑人原id  V：复制的嫌疑人对象
            Map<Integer, FunSuspectDTO> suspectMap = new HashMap<>();
            for (FunSuspectDTO thisSuspect :
                    suspectList) {
                int oriSuspectId = thisSuspect.getId();
                thisSuspect.setAjbh(oriSeq.getAjbh());
                thisSuspect.setCaseinfoid(oriCaseInfo.getId());
                thisSuspect.setSfcnumber(oriSeq.getSfcnumber());

                thisSuspect.setJqbh(oriSeq.getJqbh());
                //新建这个嫌疑人
                caseManagerService.insertNewSuspect(thisSuspect);
                suspectMap.put(oriSuspectId, thisSuspect);
            }
            createArchiveBySuspect(OriSeqId, oriSeq, suspectMap, notsuspectids);
            //新建sfc seq type record files （复制基础卷的）
            websocketSendMessage(newcasename + ",已拆案完毕!"
                    , userNow.getUsername(),
                    Enums.messagetype.typeFive);
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }


    /**
     * 通过嫌疑人复制卷
     *
     * @param notSuspectids 不被包含的嫌疑人们
     * @return |
     * @author Mrlu
     * @createTime 2021/3/8 9:04
     */
    private void createArchiveBySuspect(int oriSeqId, FunArchiveSeqDTO newSeq, Map<Integer, FunSuspectDTO> suspectMap, String notSuspectids) throws Exception {
        //查询seq下的type
        List<FunArchiveTypeDTO> archiveTypes = caseManagerService.selectArchiveTypeByJqSeq(oriSeqId);
        String[] notSuspectIds = Optional.ofNullable(notSuspectids).orElse("").split(",");
        //开始复制
        int typeOrder = 1;
        for (FunArchiveTypeDTO thisType :
                archiveTypes) {
            //查询这个卷  下的文书 只包含其中的嫌疑人
            List<FunArchiveRecordsDTO> records = caseManagerService.selectRecordsNotSuspectByType(thisType.getId(), notSuspectIds);
            //复制type
            thisType.setArchiveseqid(newSeq.getId());
            thisType.setDefaultorder(typeOrder++);//type的顺序
            thisType.setArchivesfcid(newSeq.getArchivesfcid());
            thisType.setJqbh(newSeq.getJqbh());
            thisType.setAjbh(newSeq.getAjbh());
            caseManagerService.createNewType(thisType);//insert
            for (FunArchiveRecordsDTO thisRecord :
                    records) {
                //复制文书
                int oriRecorId = thisRecord.getId();

                thisRecord.setPrevid(oriRecorId);
                thisRecord.setJqbh(thisType.getJqbh());
                thisRecord.setAjbh(thisType.getAjbh());
                thisRecord.setAuthorid(newSeq.getAuthorid());
                thisRecord.setAuthor(newSeq.getAuthor());
                thisRecord.setArchivetypeid(thisType.getId());//type
                thisRecord.setArchiveseqid(thisType.getArchiveseqid());//seq
                thisRecord.setArchivesfcid(thisType.getArchivesfcid());//sfc
                caseManagerService.createNewRecord(thisRecord);//在这里新建的
                if (thisRecord.getRecordstyle() == 1 || thisRecord.getRecordstyle() == 7) {
                    //查询原有的人文书关系表
                    //是对人的文书  复制对应关系
                    List<FunSuspectRecordDTO> sr = caseManagerService.selectSuspectRecordByRid(oriRecorId);//此时使用原有的id查询
                    //找到被复制的嫌疑人
                    if (null != sr && sr.size() > 0) {
                        for (FunSuspectRecordDTO thisRecordSuspect :
                                sr) {
                            FunSuspectDTO suspect = suspectMap.get(Optional.ofNullable(thisRecordSuspect.getSuspectid()).orElse(0));
                            if (null != suspect) {
                                thisRecordSuspect.setArchiveseqid(thisRecord.getArchiveseqid());
                                thisRecordSuspect.setRecordid(thisRecord.getId());//新的文书id
                                thisRecordSuspect.setJqbh(thisRecord.getJqbh());
                                thisRecordSuspect.setAjbh(thisRecord.getAjbh());
                                thisRecordSuspect.setSuspectid(suspect.getId());
                                thisRecordSuspect.setCaseinfoid(newSeq.getCaseinfoid());
                                thisRecordSuspect.setSfcnumber(newSeq.getSfcnumber());
                                thisRecordSuspect.setRecordtype(thisType.getRecordtype());
                                caseManagerService.createNewSR(thisRecordSuspect);//复制关系
                            }
                        }

                    }
                }
                copyFiles(caseManagerService.selectRecordFilesByRecordId(oriRecorId), thisRecord);
            }

        }

    }

    private void copyFiles(List<FunArchiveFilesDTO> fileList, FunArchiveRecordsDTO thisRecord) {
        for (FunArchiveFilesDTO thisFile :
                fileList) {
            thisFile.setFilecode(UUID.randomUUID().toString());
            thisFile.setArchiveseqid(thisRecord.getArchiveseqid());
            thisFile.setArchivesfcid(thisRecord.getArchivesfcid());
            thisFile.setArchivetypeid(thisRecord.getArchivetypeid());
            thisFile.setArchiverecordid(thisRecord.getId());
            thisFile.setAuthor(thisRecord.getAuthor());
            thisFile.setAuthorid(thisRecord.getAuthorid());
            //复制插入
            caseManagerService.createFils(thisFile);
        }
    }
}
