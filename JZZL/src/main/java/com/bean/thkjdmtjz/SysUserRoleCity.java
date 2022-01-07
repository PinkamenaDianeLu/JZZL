package com.bean.thkjdmtjz;

import java.util.Date;

/**
 * 用户角色关联表
 */
public class SysUserRoleCity {
    private Long id;

    private Integer userid;

    private Integer roleid;

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

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getRoleid() {
        return roleid;
    }

    public void setRoleid(Integer roleid) {
        this.roleid = roleid;
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