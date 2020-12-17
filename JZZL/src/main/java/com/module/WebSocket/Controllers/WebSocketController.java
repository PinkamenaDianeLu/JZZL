package com.module.WebSocket.Controllers;

import com.bean.jzgl.Source.SysUser;
import com.config.session.UserSession;
import com.config.webSocket.WebSocketConfig;
import com.config.webSocket.WebSocketMessage;
import com.module.SystemManagement.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * @author MrLu
 * @createTime 2020/8/18
 * @describe 登录
 */
@Controller
public class WebSocketController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    @Qualifier("UserServiceByRedis")
    UserService userServiceByRedis;

    @MessageMapping(WebSocketConfig.HELLO_MAPPING)
    @SendTo("/topic/greetings")
    public WebSocketMessage greeting(WebSocketMessage message) throws Exception {
        return new WebSocketMessage("nolog", "system", "您好！");
    }

    /**
     * @param
     * @author Mrlu
     * @createTime 2020/5/23 15:51
     * @describe 接受信息
     * @version 1.0
     */
    @MessageMapping("/chat")
    public void chat(WebSocketMessage message) {
        System.out.println("收到消息！");
        String returnUser=message.getfrom();
        try {
            System.out.println(returnUser);
          //  returnUser=userNow.getIdcardnumber();//发给谁
            message.setfrom("system");//发送人
            message.setMessage("就你订阅的我吗！");
        } catch (Exception e) {
            e.printStackTrace();
            message.setMessage("无法获取当前用户！");
        }
        messagingTemplate.convertAndSend("/queues/"+returnUser, message);

    }

    public void sendMes(String toUser) throws Exception {

    }
}
