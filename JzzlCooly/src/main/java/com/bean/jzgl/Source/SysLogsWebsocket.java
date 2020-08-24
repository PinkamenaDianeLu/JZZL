package com.bean.jzgl.Source;

import com.enums.Enums;

import java.util.Date;

public class SysLogsWebsocket {
    private Integer id;

    private Integer scbj;

    private Integer state;

    private Date createtime;

    private Date updatetime;

    private String sender;

    private Enums.WebSocketMessageType messagetype;

    private String receiver;

    private String serverip;

    private String clientip;

    private String message;


    @Override
    public String toString() {
        return "SysLogsWebsocket{" +
                "id=" + id +
                ", scbj=" + scbj +
                ", state=" + state +
                ", createtime=" + createtime +
                ", updatetime=" + updatetime +
                ", sender='" + sender + '\'' +
                ", messagetype=" + messagetype +
                ", receiver='" + receiver + '\'' +
                ", serverip='" + serverip + '\'' +
                ", clientip='" + clientip + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getScbj() {
        return scbj;
    }

    public void setScbj(Integer scbj) {
        this.scbj = scbj;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender == null ? null : sender.trim();
    }

    public Enums.WebSocketMessageType getMessagetype() {
        return messagetype;
    }

    public void setMessagetype(Enums.WebSocketMessageType messagetype) {
        this.messagetype = messagetype;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver == null ? null : receiver.trim();
    }

    public String getServerip() {
        return serverip;
    }

    public void setServerip(String serverip) {
        this.serverip = serverip == null ? null : serverip.trim();
    }

    public String getClientip() {
        return clientip;
    }

    public void setClientip(String clientip) {
        this.clientip = clientip == null ? null : clientip.trim();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message == null ? null : message.trim();
    }
}