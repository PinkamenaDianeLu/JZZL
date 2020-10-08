package com.module.SFCensorship.Controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bean.jzgl.Source.FunArchiveRecords;
import com.bean.jzgl.Source.FunArchiveSeq;
import com.bean.jzgl.Source.FunPeopelCase;
import com.bean.jzgl.Source.SysUser;
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
            pJsonObj.put("peopelcaseid", DecodeUrlP(pJsonObj.getString("id")));//解密的id
            reMap.put("rows", transformBmField(sFCensorshipService.selectArchiveSeqPage(pJsonObj), FunArchiveSeq.class));
            reMap.put("total", sFCensorshipService.selectArchiveSeqPageCount(pJsonObj));
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
            FunPeopelCase thisFunPeopelCase = sFCensorshipService.getFunPeopelCaseById(peopelcaseid);
            //新建送检记录
            FunArchiveSeq newSFC = new FunArchiveSeq();
            newSFC.setJqbh(thisFunPeopelCase.getJqbh());
            newSFC.setAjbh(thisFunPeopelCase.getAjbh());
            SysUser userNow = userServiceByRedis.getUserNow(null);//获取当前用户
            newSFC.setAuthor(userNow.getXm());//整理人姓名
            newSFC.setAuthoridcard(userNow.getIdcardnumber());//整理人身份证号
            newSFC.setBatchesseq(sFCensorshipService.getLastSFCSeq(peopelcaseid) + 1);//第几次送检
            //TODO MrLu 2020/10/4 卷宗编号是干啥的来着？？
            newSFC.setRecordsnumber("卷宗编号？");//卷宗编号?
            newSFC.setSfcnumber(thisFunPeopelCase.getSfcnumber());//送检编号
            newSFC.setPeopelcaseid(peopelcaseid);//案件表id
            newSFC.setIsfinal(Enums.IsFinal.NO);
            newSFC.setIssend(Enums.IsSend.NO);
            String sjlxString = pJsonObj.getString("recordscode");
            //recordscode如果为空怎么办呢？   你不会不让他空啊
            EnumSoft.sjlx thisSjlx = EnumUtils.getEnum(EnumSoft.sjlx.class, sjlxString);
            newSFC.setArchivetype(thisSjlx.getValue());//送检类型
            newSFC.setArchivename(pJsonObj.getString("archivename"));//名
            sFCensorshipService.insertSelective(newSFC);

            //得到新建的送检记录id  用此id得到对应的人
            //TODO MrLu 2020/10/4   recordsId插入
            Integer recordsId = pJsonObj.getInteger("recordsId");
            //查询关联人  插入关联人的文书和一些基础文书
            List<FunArchiveRecords> cloneRecords = sFCensorshipService.selectRecordsByJqbh(thisFunPeopelCase.getJqbh());

            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }

    private void cloneRecords(String jqbh, int seqId) {
    }
}
