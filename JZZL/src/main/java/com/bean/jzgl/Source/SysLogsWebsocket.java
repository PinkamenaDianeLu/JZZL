package com.bean.jzgl.Source;

import com.config.annotations.CodeTableMapper;

import java.util.Date;

public class SysLogsWebsocket {
    private Integer id;

    private Integer scbj;

    private Integer state;

    private Date createtime;

    private Date updatetime;

    private String sender;

    private String serverip;

    private Integer messagetype;
    @CodeTableMapper(sourceFiled = "messagetype", codeTableType = "messagetype")
    private String messagetype_name;

    private String message;

    private String receiver;

    private Date receivertime;

    private String receiverip;

    private Integer isreceived;

    private Integer issended;

    private Date sendtime;

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

    public String getServerip() {
        return serverip;
    }

    public void setServerip(String serverip) {
        this.serverip = serverip == null ? null : serverip.trim();
    }

    public Integer getMessagetype() {
        return messagetype;
    }

    public void setMessagetype(Integer messagetype) {
        this.messagetype = messagetype;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message == null ? null : message.trim();
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver == null ? null : receiver.trim();
    }

    public Date getReceivertime() {
        return receivertime;
    }

    public void setReceivertime(Date receivertime) {
        this.receivertime = receivertime;
    }

    public String getReceiverip() {
        return receiverip;
    }

    public void setReceiverip(String receiverip) {
        this.receiverip = receiverip == null ? null : receiverip.trim();
    }

    public Integer getIsreceived() {
        return isreceived;
    }

    public void setIsreceived(Integer isreceived) {
        this.isreceived = isreceived;
    }

    public String getMessagetype_name() {
        return messagetype_name;
    }

    public void setMessagetype_name(String messagetype_name) {
        this.messagetype_name = messagetype_name;
    }

    public Integer getIssended() {
        return issended;
    }

    public void setIssended(Integer issended) {
        this.issended = issended;
    }

    public Date getSendtime() {
        return sendtime;
    }

    public void setSendtime(Date sendtime) {
        this.sendtime = sendtime;
    }
}