package com.module.CaseManager.Controllers;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.bean.jzgl.DTO.FunCaseInfoDTO;
import com.bean.jzgl.DTO.FunCasePeoplecaseDTO;
import com.bean.jzgl.DTO.SysUserDTO;
import com.config.annotations.OperLog;
import com.enums.Enums;
import com.module.CaseManager.Services.CaseManagerService;
import com.module.CaseManager.Services.CaseSearchService;
import com.module.SystemManagement.Services.UserService;
import com.util.EnumsUtil;
import com.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author MrLu
 * @createTime 2021/1/8 15:17
 * @describe 案件管理  合案、拆案
 */
@Controller
@RequestMapping("/CaseManager")
public class CaseManagerController {
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
     * 合案
     * @author MrLu
     * @param
     * @createTime  2021/1/8 16:45
     * @return    |
      */
     @RequestMapping(value = "/combinationCase", method = {RequestMethod.GET,
             RequestMethod.POST})
     @ResponseBody
     @OperLog(operModul = operModul, operDesc = "合案", operType = OperLog.type.INSERT)
     public  String  combinationCase(Integer mainCaseId,String affiliateds, String newcasename){
        JSONObject reValue = new JSONObject();
                try {
                    FunCaseInfoDTO mainCaseInfo = caseManagerService.selectCaseInfoById(mainCaseId);
                    SysUserDTO zbr = caseManagerService.selectSysUserDtoById(mainCaseInfo.getBarsysuserid());
                    String dwdm = zbr.getAgencycode().substring(0, 6);
                    mainCaseInfo.setJqbh(StringUtil.createJqbh(dwdm));
                    mainCaseInfo.setAjbh(StringUtil.createAjbh(dwdm));
                    mainCaseInfo.setCasename(newcasename);
                    mainCaseInfo.setSfcnumber(StringUtil.createSfcNumber(zbr.getAgencycode().substring(0, 6)));//送检编号
                    mainCaseInfo.setCasestate(Enums.CaseState.MERGE.getValue());//拆案
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
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/8 15:45
     */
    @RequestMapping(value = "/splitCase", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "拆案", operType = OperLog.type.INSERT)
    public String splitCase(Integer oricaseid, Integer newzbrid, String newcasename) {
        JSONObject reValue = new JSONObject();
        try {

            FunCaseInfoDTO oriCaseInfo = caseManagerService.selectCaseInfoById(oricaseid);
            SysUserDTO zbr = caseManagerService.selectSysUserDtoById(newzbrid);

            String dwdm = zbr.getAgencycode().substring(0, 6);
            oriCaseInfo.setJqbh(StringUtil.createJqbh(dwdm));
            oriCaseInfo.setAjbh(StringUtil.createAjbh(dwdm));
            oriCaseInfo.setCasename(newcasename);
            oriCaseInfo.setSfcnumber(StringUtil.createSfcNumber(zbr.getAgencycode().substring(0, 6)));//送检编号
            oriCaseInfo.setCasestate(Enums.CaseState.SPLIT.getValue());//拆案
            oriCaseInfo.setBarxm(zbr.getXm());
            oriCaseInfo.setBarsysuserid(zbr.getId());
            oriCaseInfo.setBaridcard(zbr.getIdcardnumber());
            oriCaseInfo.setBadwdwdm(zbr.getAgencycode());
            oriCaseInfo.setBadwdwmc(zbr.getAgencyname());
            oriCaseInfo.setGajgmc("公安机关名称");
            oriCaseInfo.setGajgdm("公安机关代码");
            //新建案件
            caseManagerService.insertCaseinfo(oriCaseInfo);

            //复制关系
            List<FunCasePeoplecaseDTO> relations = caseManagerService.selectRelationByCaseid(oricaseid);
            for (FunCasePeoplecaseDTO thisRelations :
                    relations) {
                if (thisRelations.getPersontype() == Enums.PersonType.PRIMARY.getValue()) {
                    //主办人不复制
                    continue;
                }
                thisRelations.setJqbh(oriCaseInfo.getJqbh());//警情编号
                thisRelations.setAjbh(oriCaseInfo.getAjbh());//案件编号
                thisRelations.setPersontype(Enums.PersonType.SECONDARY.getValue());//人员类型
                thisRelations.setSfcnumber(oriCaseInfo.getSfcnumber());//送检编号
                thisRelations.setCasename(oriCaseInfo.getCasename());//案件名称
                thisRelations.setCaseinfoid(oriCaseInfo.getId());//案件表id
                thisRelations.setBarxm(oriCaseInfo.getBarxm());//主办人姓名
                thisRelations.setBarsysuserid(oriCaseInfo.getBarsysuserid());//主办人id
                thisRelations.setBaridcard(oriCaseInfo.getBaridcard());//主办人身份证号
                thisRelations.setBadwdwdm(oriCaseInfo.getBadwdwdm());//办案单位代码
                thisRelations.setBadwdwmc(oriCaseInfo.getBadwdwmc());
                caseManagerService.insertCasePeopleCase(thisRelations);
            }
            //新建sfc seq type record files （复制基础卷的）
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }
}
