package com.bean.jzgl.Source;

import com.config.annotations.CodeTableMapper;

import java.util.Date;

public class SysRecordorder {
    private Integer id;

    private Integer scbj;

    private Integer state;

    private Date createtime;

    private Date updatetime;

    private String recordname;

    private String recordcode;

    private Integer defaultorder;

    private Integer recordtype;
    @CodeTableMapper(sourceFiled = "recordtype", codeTableType = "archivetype")
    private String recordtype_name;

    private Integer archivetype;
    @CodeTableMapper(sourceFiled = "archivetype", codeTableType = "archivetype")
    private String archivetype_name;

    private Integer recordstyle;

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

    public String getRecordcode() {
        return recordcode;
    }

    public void setRecordcode(String recordcode) {
        this.recordcode = recordcode == null ? null : recordcode.trim();
    }

    public Integer getDefaultorder() {
        return defaultorder;
    }

    public void setDefaultorder(Integer defaultorder) {
        this.defaultorder = defaultorder;
    }

    public Integer getRecordtype() {
        return recordtype;
    }

    public void setRecordtype(Integer recordtype) {
        this.recordtype = recordtype;
    }

    public Integer getArchivetype() {
        return archivetype;
    }

    public void setArchivetype(Integer archivetype) {
        this.archivetype = archivetype;
    }

    public String getRecordtype_name() {
        return recordtype_name;
    }

    public void setRecordtype_name(String recordtype_name) {
        this.recordtype_name = recordtype_name;
    }

    public String getArchivetype_name() {
        return archivetype_name;
    }

    public void setArchivetype_name(String archivetype_name) {
        this.archivetype_name = archivetype_name;
    }

    public Integer getRecordstyle() {
        return recordstyle;
    }

    public void setRecordstyle(Integer recordstyle) {
        this.recordstyle = recordstyle;
    }
}