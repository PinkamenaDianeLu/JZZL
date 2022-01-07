package com.bean.thkjdmtjz;

import java.util.Date;

public class SysUserGroup {
    private Long id;

    private String username;

    private String gxgajgmc;

    private String gxgajgjgdm;

    private Integer status;

    private String scbj;

    private Date createtime;

    private Date updatetime;

    private String createxm;

    private String createzjhm;

    private String createdwmc;

    private String createdwdm;

    public String getCreatexm() {
        return createxm;
    }

    public void setCreatexm(String createxm) {
        this.createxm = createxm;
    }

    public String getCreatezjhm() {
        return createzjhm;
    }

    public void setCreatezjhm(String createzjhm) {
        this.createzjhm = createzjhm;
    }

    public String getCreatedwmc() {
        return createdwmc;
    }

    public void setCreatedwmc(String createdwmc) {
        this.createdwmc = createdwmc;
    }

    public String getCreatedwdm() {
        return createdwdm;
    }

    public void setCreatedwdm(String createdwdm) {
        this.createdwdm = createdwdm;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getGxgajgmc() {
        return gxgajgmc;
    }

    public void setGxgajgmc(String gxgajgmc) {
        this.gxgajgmc = gxgajgmc == null ? null : gxgajgmc.trim();
    }

    public String getGxgajgjgdm() {
        return gxgajgjgdm;
    }

    public void setGxgajgjgdm(String gxgajgjgdm) {
        this.gxgajgjgdm = gxgajgjgdm == null ? null : gxgajgjgdm.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getScbj() {
        return scbj;
    }

    public void setScbj(String scbj) {
        this.scbj = scbj == null ? null : scbj.trim();
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
}