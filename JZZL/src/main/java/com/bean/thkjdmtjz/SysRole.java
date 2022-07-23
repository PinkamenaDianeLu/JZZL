package com.bean.thkjdmtjz;

import java.util.Date;

/**
 * @Author       leewe
 * @createTime   2020/11/12 8:44
 * @Description  角色信息表
 */
public class SysRole {
    private Long id;
    private String rolename;
    private String rolekey;
    private String datascope;
    private Date createtime;

    private Integer rolesort;

    private String status;

    private String scbj;

    private String remark;

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

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename == null ? null : rolename.trim();
    }

    public String getRolekey() {
        return rolekey;
    }

    public void setRolekey(String rolekey) {
        this.rolekey = rolekey == null ? null : rolekey.trim();
    }

    public Integer getRolesort() {
        return rolesort;
    }

    public void setRolesort(Integer rolesort) {
        this.rolesort = rolesort;
    }

    public String getDatascope() {
        return datascope;
    }

    public void setDatascope(String datascope) {
        this.datascope = datascope == null ? null : datascope.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}