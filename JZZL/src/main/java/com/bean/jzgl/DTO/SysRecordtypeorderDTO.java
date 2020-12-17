package com.bean.jzgl.DTO;

import java.util.Date;

public class SysRecordtypeorderDTO {
    private Integer id;

    private Integer scbj;

    private Integer state;

    private Date createtime;

    private Date updatetime;

    private String recordname;

    private Integer recordcode;

    private Integer defaultorder;

    private Integer archivetype;

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

    public Integer getRecordcode() {
        return recordcode;
    }

    public void setRecordcode(Integer recordcode) {
        this.recordcode = recordcode;
    }

    public Integer getDefaultorder() {
        return defaultorder;
    }

    public void setDefaultorder(Integer defaultorder) {
        this.defaultorder = defaultorder;
    }

    public Integer getArchivetype() {
        return archivetype;
    }

    public void setArchivetype(Integer archivetype) {
        this.archivetype = archivetype;
    }
}