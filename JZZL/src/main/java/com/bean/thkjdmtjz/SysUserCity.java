package com.bean.thkjdmtjz;


import java.util.Date;
import java.util.List;

/**
 * 用户信息表
 */
public class SysUserCity {

    private Long id;
    private String username;
    private String xm;
    private String dwmc;
    private String dwmcdm;
    private Date createtime;

    private String zjmc;

    private String jh;

    private String password;

    private String phone;

    private Integer state;

    private Integer scbj;

    private Date updatetime;

    private String origin;

    private String remark;

    private String createxm;

    private String createzjhm;

    private String createdwmc;

    private String createdwdm;

    public String getZjmc() {
        return zjmc;
    }

    public void setZjmc(String zjmc) {
        this.zjmc = zjmc;
    }

    public String getJh() {
        return jh;
    }

    public void setJh(String jh) {
        this.jh = jh;
    }

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


    private List<SysGroup> sysUserGroups;//用户管辖单位关联表

    public List<SysGroup> getSysUserGroups() {
        return sysUserGroups;
    }

    public void setSysUserGroups(List<SysGroup> sysUserGroups) {
        this.sysUserGroups = sysUserGroups;
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


    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm == null ? null : xm.trim();
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getDwmc() {
        return dwmc;
    }

    public void setDwmc(String dwmc) {
        this.dwmc = dwmc == null ? null : dwmc.trim();
    }

    public String getDwmcdm() {
        return dwmcdm;
    }

    public void setDwmcdm(String dwmcdm) {
        this.dwmcdm = dwmcdm == null ? null : dwmcdm.trim();
    }

    public String getPassword() {
        return password;
    }
}