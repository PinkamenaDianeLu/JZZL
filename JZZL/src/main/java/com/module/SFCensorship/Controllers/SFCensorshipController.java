package com.module.SFCensorship.Controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bean.jzgl.DTO.FunArchiveFilesDTO;
import com.bean.jzgl.DTO.FunArchiveRecordsDTO;
import com.bean.jzgl.DTO.FunArchiveTypeDTO;
import com.bean.jzgl.Source.*;
import com.config.annotations.OperLog;
import com.enums.EnumSoft;
import com.enums.Enums;
import com.factory.BaseFactory;
import com.module.SFCensorship.Services.SFCensorshipService;
import com.module.SystemManagement.Services.UserService;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            pJsonObj.put("peoplecaseid", DecodeUrlP(pJsonObj.getString("id")));//解密的id
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
            String id = pJsonObj.getString("peopelcaseid");
            if (StringUtils.isEmpty(id)) {
                throw new Exception("关键参数：peopelcaseid 未传递");
            }
            int peopelcaseid = Integer.parseInt(DecodeUrlP(id));//得到解密后的案件表id
            FunCaseInfo thisFunPeopelCase = sFCensorshipService.getFunCaseInfoById(peopelcaseid);
            SysUser userNow = userServiceByRedis.getUserNow(null);//获取当前用户
            //新建送检记录
            FunArchiveSFC newSfc=new FunArchiveSFC();
            newSfc.setJqbh(thisFunPeopelCase.getJqbh());
            newSfc.setAjbh(thisFunPeopelCase.getAjbh());
            newSfc.setIssend(Enums.IsSend.NO);
            newSfc.setAuthor(userNow.getXm());
            newSfc.setAuthoridcard(userNow.getIdcardnumber());
            newSfc.setPeoplecaseid(peopelcaseid);
            String sjlxString = pJsonObj.getString("recordscode");
            //recordscode如果为空怎么办呢？   你不会不让他空啊
            EnumSoft.sjlx thisSjlx = EnumUtils.getEnum(EnumSoft.sjlx.class, sjlxString);
            newSfc.setSfcnumber(thisFunPeopelCase.getSfcnumber());//送检编号
            newSfc.setArchivetype(thisSjlx.getValue());//
            newSfc.setArchivename(pJsonObj.getString("archivename"));
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
            newSeq.setPeopelcaseid(newSfc.getPeoplecaseid());//案件表id
            newSeq.setIsfinal(Enums.IsFinal.NO);//是否是完结版
            newSeq.setSfcnumber(newSfc.getSfcnumber());//送检编号
            newSeq.setArchivetype(newSfc.getArchivetype());//送检类型
            newSeq.setArchivename(newSfc.getArchivename());//送检名
            sFCensorshipService.insertFunArchiveSeq(newSeq);

            //得到新建的送检记录id  用此id得到对应的人
            //TODO MrLu 2020/10/4   recordsId插入
//            Integer recordsId = pJsonObj.getInteger("recordsId");
            //查询关联人  插入关联人的文书和一些基础文书

            //创建新建卷
            //TODO MrLu 2020/10/8   未对应人筛选文书
             cloneRecords(thisFunPeopelCase.getJqbh(), newSeq);
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
     * @param jqbh  警情编号
     * @param newSeq 送检记录表
     * @return void  |
     * @author MrLu
     * @createTime 2020/10/8 13:55
     */
    private void cloneRecords(String jqbh, final FunArchiveSeq newSeq) {
        List<FunArchiveTypeDTO> oriArchiveTypes = sFCensorshipService.selectArchiveTypeByJqSeq(jqbh,0);
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
            FunArchiveRecordsDTO cover=new FunArchiveRecordsDTO();
            cover.setJqbh(thisOriAt.getJqbh());
            cover.setAjbh(thisOriAt.getAjbh());
            cover.setThisorder(EnumSoft.fplx.COVER.getOrder());
            cover.setRecordname(EnumSoft.fplx.COVER.getName());
            cover.setArchivetypeid(newTypeId);
            cover.setArchivecode(thisOriAt.getArchivecode());
            cover.setArchivesfcid(newSeq.getArchivesfcid());
            cover.setRecordstyle(0);
            cover.setAuthor(newSeq.getAuthor());//整理人姓名
            cover.setAuthorid(newSeq.getAuthorid());//整理人id
            cover.setIsazxt(1);//封皮、目录、封底 都不是安综原有的东西
            cover.setArchiveseqid(newSeq.getId());
            cover.setRecordscode(EnumSoft.fplx.COVER.getValue());//文件代码
            sFCensorshipService.insertFunArchiveRecords(cover,thisOriAt);
            //文书目录
            cover.setThisorder(EnumSoft.fplx.INDEX.getOrder());
            cover.setRecordname(EnumSoft.fplx.INDEX.getName());
            cover.setRecordscode(EnumSoft.fplx.INDEX.getValue());//文件代码
            sFCensorshipService.insertFunArchiveRecords(cover,thisOriAt);
            //封底
            cover.setThisorder(EnumSoft.fplx.BACKCOVER.getOrder());
            cover.setRecordname(EnumSoft.fplx.BACKCOVER.getName());
            cover.setRecordscode(EnumSoft.fplx.BACKCOVER.getValue());//文件代码
            sFCensorshipService.insertFunArchiveRecords(cover,thisOriAt);


            for (FunArchiveRecordsDTO thisRecord :
                    cloneRecords) {
                thisRecord.setArchivesfcid(newSeq.getArchivesfcid());//送检次序id
                thisRecord.setArchiveseqid(newSeq.getId());//整理次序id
                thisRecord.setArchivetypeid(newTypeId);//对应了新的archiveType表id
                //这里不要改变它的所属人和id 应为这是他们还是安综抽过来的 文件没有任何变化
                sFCensorshipService.insertFunArchiveRecords(thisRecord,thisOriAt);
            }
        }
    }
}
