package com.config.webSocket;

import java.util.Date;

/**
 * @author Mrlu
 * @createTime 2020/5/23
 * @describe  WebSocket信息实体类
 */

public class WebSocketMessage  {
    private  String receiver; //发送给谁
    private  String sender;//消息来源
    private  String message;//信息
    private  Integer messagetype;
    private Date sendtime;

    public WebSocketMessage(String receiver, String sender, String message, Integer messagetype, Date sendtime) {
        this.receiver = receiver;
        this.sender = sender;
        this.message = message;
        this.messagetype = messagetype;
        this.sendtime = sendtime;
    }

    public WebSocketMessage() {
    }

    @Override
    public String toString() {
        return "WebSocketMessage{" +
                "receiver='" + receiver + '\'' +
                ", sender='" + sender + '\'' +
                ", message='" + message + '\'' +
                ", messagetype=" + messagetype +
                ", sendtime=" + sendtime +
                '}';
    }

    public String getreceiver() {
        return receiver;
    }

    public void setreceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getsender() {
        return sender;
    }

    public void setsender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getMessagetype() {
        return messagetype;
    }

    public void setMessagetype(Integer messagetype) {
        this.messagetype = messagetype;
    }

    public Date getSendtime() {
        return sendtime;
    }

    public void setSendtime(Date sendtime) {
        this.sendtime = sendtime;
    }
}
