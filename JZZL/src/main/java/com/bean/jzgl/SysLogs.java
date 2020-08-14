package com.bean.jzgl;

import java.util.Date;

public class SysLogs {
    private Integer id;

    private Integer scbj;

    private Integer state;

    private Date createtime;

    private Date updatetime;

    private String mparams;

    private String requesturl;

    private String mname;

    private String mresult;

    private String operdesc;

    private String opermodul;

    private String opertype;

    private String ip;

    private Integer sysuserid;

    private String sysusername;

    private String operator;

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

    public String getMparams() {
        return mparams;
    }

    public void setMparams(String mparams) {
        this.mparams = mparams == null ? null : mparams.trim();
    }

    public String getRequesturl() {
        return requesturl;
    }

    public void setRequesturl(String requesturl) {
        this.requesturl = requesturl == null ? null : requesturl.trim();
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname == null ? null : mname.trim();
    }

    public String getMresult() {
        return mresult;
    }

    public void setMresult(String mresult) {
        this.mresult = mresult == null ? null : mresult.trim();
    }

    public String getOperdesc() {
        return operdesc;
    }

    public void setOperdesc(String operdesc) {
        this.operdesc = operdesc == null ? null : operdesc.trim();
    }

    public String getOpermodul() {
        return opermodul;
    }

    public void setOpermodul(String opermodul) {
        this.opermodul = opermodul == null ? null : opermodul.trim();
    }

    public String getOpertype() {
        return opertype;
    }

    public void setOpertype(String opertype) {
        this.opertype = opertype == null ? null : opertype.trim();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public Integer getSysuserid() {
        return sysuserid;
    }

    public void setSysuserid(Integer sysuserid) {
        this.sysuserid = sysuserid;
    }

    public String getSysusername() {
        return sysusername;
    }

    public void setSysusername(String sysusername) {
        this.sysusername = sysusername == null ? null : sysusername.trim();
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }
}