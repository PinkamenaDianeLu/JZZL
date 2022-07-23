package com.module.WebSocket.Services.Impl;

import com.bean.jzgl.DTO.SysLogsWebsocketDTO;
import com.config.webSocket.WebSocketMessage;
import com.mapper.jzgl.SysLogsWebsocketDTOMapper;
import com.module.WebSocket.Services.WebSocketService;
import com.util.IpUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @author MrLu
 * @createTime 2020/12/18 10:04
 * @describe
 */
@Service
public class WebSocketImpl implements WebSocketService {

    @Resource
    SysLogsWebsocketDTOMapper sysLogsWebsocketDTOMapper;

    @Override
    public void insertWebSocketLog(WebSocketMessage message, boolean isSend) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        SysLogsWebsocketDTO newLog = new SysLogsWebsocketDTO();

        /*newLog.setReceivertime();//接收时间
        newLog.setReceiverip();//接收ip
        newLog.setIssended();//是否已发送
        //newLog.setSendtime();// 发送时间
            */
        newLog.setMessage(message.getMessage());//发送信息
        newLog.setReceiver(message.getreceiver());//接收人
        newLog.setSender(message.getsender());//发送人
        newLog.setServerip(IpUtil.getIpAddress(request));//发送ip
        newLog.setIsreceived(0);//未接收
        newLog.setMessagetype(message.getMessagetype());//消息类型
        //判断是否已发送
        if (isSend) {
            newLog.setIssended(1);//已经发送
            newLog.setSendtime(new Date());//发送时间
        } else {
            newLog.setIssended(0);
        }

        sysLogsWebsocketDTOMapper.insertSelective(newLog);
    }

    @Override
    public List<SysLogsWebsocketDTO> selectUnreadMessageByUsername(String username) {
        return sysLogsWebsocketDTOMapper.selectMessageByUsername(0,username);
    }

    @Override
    public void readMessage(String[] ids) {
        sysLogsWebsocketDTOMapper.readMessage(ids);
    }

    @Override
    public void readMessageAll(String receiver) {
        sysLogsWebsocketDTOMapper.readMessageAll(receiver);
    }
}
