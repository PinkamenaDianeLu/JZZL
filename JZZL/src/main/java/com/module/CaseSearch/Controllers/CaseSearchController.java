package com.module.CaseSearch.Controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.config.aop.OperLog;
import com.module.CaseSearch.Services.CaseSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MrLu
 * @createTime 2020/8/19
 * @describe  案件查询  查询用户的案件信息
 */

@Controller
@RequestMapping("/CaseSearch")
public class CaseSearchController {
    private  final  String operModul="CaseSearch";

    final
    CaseSearchService caseSearchService;
    @Autowired
    public CaseSearchController(CaseSearchService caseSearchService) {
        this.caseSearchService = caseSearchService;
    }

    @RequestMapping(value = "/test", method = {RequestMethod.GET,
            RequestMethod.POST}, produces = "text/html;charset=UTF-8")
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "test", operType = OperLog.type.INSERT)
    public void test (){
//        caseSearchService.testInsert();
        JSONObject reValue = new JSONObject();
                try {
                    System.out.println(caseSearchService.testSearch().getPersontype());
                    reValue.put("message", "success");
                } catch (Exception e) {
                    e.printStackTrace();
                    reValue.put("message", "error");
                }

    }


     /**
     * 分页查询案件列表
     * @author MrLu
     * @param 
     * @createTime  2020/9/23 14:50
     * @return    |  
      */
    @RequestMapping(value = "/selectPeopleCasePage", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "案件分页查询", operType = OperLog.type.SELECT)
    public Map<String, Object> selectPeopleCasePage(Integer offset, Integer limit, String params){
        Map<String, Object> reMap = new HashMap<>();
        try {
            //参数列表
            JSONObject pJsonObj = JSON.parseObject(params);
            pJsonObj.put("pageStart", String.valueOf((offset - 1) * limit));
            pJsonObj.put("pageEnd", String.valueOf((offset) * limit));
            reMap.put("rows", caseSearchService.selectPeopleCasePageTest(pJsonObj));
            reMap.put("total", caseSearchService.selectPeopleCasePageCount(pJsonObj));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reMap;
    }
}
