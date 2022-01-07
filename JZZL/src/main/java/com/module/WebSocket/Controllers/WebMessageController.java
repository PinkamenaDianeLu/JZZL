package com.module.WebSocket.Controllers;

import com.alibaba.fastjson.JSONObject;
import com.bean.jzgl.Source.SysUser;
import com.config.annotations.OperLog;
import com.module.SystemManagement.Services.UserService;
import com.module.WebSocket.Services.WebSocketService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author MrLu
 * @createTime 2020/12/18 14:54
 * @describe
 */

@Controller
@RequestMapping("/WebMessage")
public class WebMessageController {

    @Autowired
    @Qualifier("UserServiceByRedis")
    UserService userServiceByRedis;

    @Autowired
    com.module.WebSocket.Services.WebSocketService webSocketService;

    @RequestMapping(value = "/selectUnreadMessage", method = {RequestMethod.GET,
            RequestMethod.POST}, produces = "text/html;charset=UTF-8")
    @ResponseBody
    @OperLog(operModul = "WebMessage", operDesc = "查询当前用户的未读消息", operType = OperLog.type.SELECT)
    public String selectUnreadMessage() {
        JSONObject reValue = new JSONObject();
        try {
            SysUser userNow = userServiceByRedis.getUserNow(null);

            reValue.put("value", webSocketService.selectUnreadMessageByUsername(userNow.getUsername()));
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }


     /**
     * 确认消息
     * @author MrLu
     * @param ids  1,2,3,4 id
     * @createTime  2020/12/18 16:40
     * @return    |
      */
    @RequestMapping(value = "/readMessage", method = {RequestMethod.GET,
            RequestMethod.POST}, produces = "text/html;charset=UTF-8")
    @ResponseBody
    @OperLog(operModul = "WebMessage", operDesc = "确认消息", operType = OperLog.type.UPDATE)
    public String readMessage(String ids) {
        JSONObject reValue = new JSONObject();
        try {
            if (StringUtils.isBlank(ids)) {
                //全部已读
                //把这个用户的全部已读
                SysUser userNow = userServiceByRedis.getUserNow(null);
                webSocketService.readMessageAll(userNow.getIdcardnumber());
            }else {
                String[] idAry = ids.split(",");
                webSocketService.readMessage(idAry);
            }

            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }
}
