package com.bean.jzgl.Source;

import java.util.Date;

public class SysRecordMessage {
    private Integer id;

    private Integer scbj;

    private Integer state;

    private Date createtime;

    private Date updatetime;

    private String recordname;

    private String wjbm;

    private String recordcode;

    private String jcycode;

    private String z;

    private String difference;

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

    public String getRecordname() {
        return recordname;
    }

    public void setRecordname(String recordname) {
        this.recordname = recordname == null ? null : recordname.trim();
    }

    public String getWjbm() {
        return wjbm;
    }

    public void setWjbm(String wjbm) {
        this.wjbm = wjbm == null ? null : wjbm.trim();
    }

    public String getRecordcode() {
        return recordcode;
    }

    public void setRecordcode(String recordcode) {
        this.recordcode = recordcode == null ? null : recordcode.trim();
    }

    public String getJcycode() {
        return jcycode;
    }

    public void setJcycode(String jcycode) {
        this.jcycode = jcycode == null ? null : jcycode.trim();
    }

    public String getZ() {
        return z;
    }

    public void setZ(String z) {
        this.z = z == null ? null : z.trim();
    }

    public String getDifference() {
        return difference;
    }

    public void setDifference(String difference) {
        this.difference = difference == null ? null : difference.trim();
    }
}