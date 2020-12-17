package com.module.CaseSearch.Controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bean.jzgl.Source.SysUser;
import com.config.annotations.OperLog;
import com.config.webSocket.WebSocketMessage;
import com.module.CaseSearch.Services.CaseSearchService;
import com.module.SystemManagement.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MrLu
 * @createTime 2020/8/19
 * @describe 案件查询  查询用户的案件信息
 */

@Controller
@RequestMapping("/CaseSearch")
public class CaseSearchController {
    private final String operModul = "CaseSearch";
    private final UserService userServiceByRedis;
    private final
    CaseSearchService caseSearchService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    public CaseSearchController(@Qualifier("UserServiceByRedis") UserService userServiceByRedis, CaseSearchService caseSearchService) {
        this.userServiceByRedis = userServiceByRedis;
        this.caseSearchService = caseSearchService;
    }





    /**
     * 分页查询案件列表
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2020/9/23 14:50
     */
    @RequestMapping(value = "/selectPeopleCasePage", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "案件分页查询", operType = OperLog.type.SELECT)
    public Map<String, Object> selectPeopleCasePage(Integer offset, Integer limit, String params) {
        Map<String, Object> reMap = new HashMap<>();
        try {
            //参数列表
            JSONObject pJsonObj = JSON.parseObject(params);
            pJsonObj.put("pageStart", String.valueOf((offset - 1) * limit));
            pJsonObj.put("pageEnd", String.valueOf((offset) * limit));
            SysUser userNow = userServiceByRedis.getUserNow(null);//获取当前用户
            pJsonObj.put("sysuserid",userNow.getId());
            reMap.put("rows", caseSearchService.selectPeopleCasePage(pJsonObj));
            reMap.put("total", caseSearchService.selectPeopleCasePageCount(pJsonObj));
            WebSocketMessage message=new WebSocketMessage();
            message.setMessage("我是从查案子来的");
            message.setfrom("system");
            message.setto(userNow.getIdcardnumber());
            messagingTemplate.convertAndSend("/queues/"+userNow.getIdcardnumber(), message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reMap;
    }
}
