package com.bean.thkjdmtjz;

import java.util.Date;

/**
 * @Author       leewe
 * @createTime   2020/11/12 8:43
 * @Description  机构数据表
 */
public class SysGroup {
    private Long id;
    private String dwmc;
    private String dwjc;
    private Date createtime;

    private String dwmcdm;

    private Integer state;

    private Integer scbj;

    private String olddwdm;

    public String getOlddwdm() {
        return olddwdm;
    }

    public void setOlddwdm(String olddwdm) {
        this.olddwdm = olddwdm;
    }
    private Date updatetime;

    private String origin;

    public String getDwjc() {
        return dwjc;
    }

    public void setDwjc(String dwjc) {
        this.dwjc = dwjc;
    }

    private String createxm;

    private String createzjhm;

    private String createdwmc;

    private String createdwdm;

    private String jzdm;

    private String jzzw;

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

    public String getJzdm() {
        return jzdm;
    }

    public void setJzdm(String jzdm) {
        this.jzdm = jzdm;
    }

    public String getJzzw() {
        return jzzw;
    }

    public void setJzzw(String jzzw) {
        this.jzzw = jzzw;
    }
}