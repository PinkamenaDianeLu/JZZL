package com.bean.thkjdmtjz;

import java.util.Date;

public class SysRoleRole {
    private Long id;

    private Integer fromroleid;

    private String fromrolename;

    private Integer toroleid;

    private String torolename;

    private Integer state;

    private Integer scbj;

    private Date createtime;

    private Date updatetime;

    private String origin;

    private String createxm;

    private String createzjhm;

    private String createdwmc;

    private String createdwdm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFromroleid() {
        return fromroleid;
    }

    public void setFromroleid(Integer fromroleid) {
        this.fromroleid = fromroleid;
    }

    public String getFromrolename() {
        return fromrolename;
    }

    public void setFromrolename(String fromrolename) {
        this.fromrolename = fromrolename == null ? null : fromrolename.trim();
    }

    public Integer getToroleid() {
        return toroleid;
    }

    public void setToroleid(Integer toroleid) {
        this.toroleid = toroleid;
    }

    public String getTorolename() {
        return torolename;
    }

    public void setTorolename(String torolename) {
        this.torolename = torolename == null ? null : torolename.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getScbj() {
        return scbj;
    }

    public void setScbj(Integer scbj) {
        this.scbj = scbj;
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

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin == null ? null : origin.trim();
    }

    public String getCreatexm() {
        return createxm;
    }

    public void setCreatexm(String createxm) {
        this.createxm = createxm == null ? null : createxm.trim();
    }

    public String getCreatezjhm() {
        return createzjhm;
    }

    public void setCreatezjhm(String createzjhm) {
        this.createzjhm = createzjhm == null ? null : createzjhm.trim();
    }

    public String getCreatedwmc() {
        return createdwmc;
    }

    public void setCreatedwmc(String createdwmc) {
        this.createdwmc = createdwmc == null ? null : createdwmc.trim();
    }

    public String getCreatedwdm() {
        return createdwdm;
    }

    public void setCreatedwdm(String createdwdm) {
        this.createdwdm = createdwdm == null ? null : createdwdm.trim();
    }
}