package com.module.CaseSearch.Controllers;

import com.bean.jzgl.Source.FunPeopelCase;
import com.config.aop.OperLog;
import com.module.CaseSearch.Services.CaseSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author MrLu
 * @createTime 2020/8/19
 * @describe  案件查询  查询用户的案件信息
 */

@Controller
@RequestMapping("/CaseSearch")
public class CaseSearchController {

    @Autowired
    CaseSearchService caseSearchService;
    @RequestMapping(value = "/test", method = {RequestMethod.GET,
            RequestMethod.POST}, produces = "text/html;charset=UTF-8")
    @ResponseBody
    @OperLog(operModul = "index", operDesc = "test", operType = OperLog.type.INSERT)
    public void test (){
//        caseSearchService.testInsert();
        for (FunPeopelCase thisa:
        caseSearchService.testSearch()) {
            System.out.println(thisa.getPersontype());
        }
    }
}
