package com.bean.jzgl.Source;

import com.config.annotations.CodeTableMapper;

import java.util.Date;


public class FunArchiveRecords {
    private Integer id;

    private Integer scbj;

    private Integer state;

    private Date createtime;

    private Date updatetime;

    private String jqbh;

    private String ajbh;

    private Integer thisorder;

    private String recordname;
    private Integer archivetypeid;
    private String archivecode;
    private Integer archiveseqid;
    private String recordstyle;
    @CodeTableMapper(sourceFiled = "recordstyle", codeTableType = "recordstyle")
    private String recordstyle_name;
    private  String recordscode;
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

    public String getJqbh() {
        return jqbh;
    }

    public void setJqbh(String jqbh) {
        this.jqbh = jqbh == null ? null : jqbh.trim();
    }

    public String getAjbh() {
        return ajbh;
    }

    public void setAjbh(String ajbh) {
        this.ajbh = ajbh == null ? null : ajbh.trim();
    }

    public Integer getThisorder() {
        return thisorder;
    }

    public void setThisorder(Integer thisorder) {
        this.thisorder = thisorder;
    }

    public String getRecordname() {
        return recordname;
    }

    public void setRecordname(String recordname) {
        this.recordname = recordname == null ? null : recordname.trim();
    }

    public Integer getArchivetypeid() {
        return archivetypeid;
    }

    public void setArchivetypeid(Integer archivetypeid) {
        this.archivetypeid = archivetypeid;
    }

    public String getArchivecode() {
        return archivecode;
    }

    public void setArchivecode(String archivecode) {
        this.archivecode = archivecode;
    }

    public String getRecordstyle() {
        return recordstyle;
    }

    public void setRecordstyle(String recordstyle) {
        this.recordstyle = recordstyle;
    }

    public String getRecordstyle_name() {
        return recordstyle_name;
    }

    public void setRecordstyle_name(String recordstyle_name) {
        this.recordstyle_name = recordstyle_name;
    }

    public Integer getArchiveseqid() {
        return archiveseqid;
    }

    public void setArchiveseqid(Integer archiveseqid) {
        this.archiveseqid = archiveseqid;
    }

    public String getRecordscode() {
        return recordscode;
    }

    public void setRecordscode(String recordscode) {
        this.recordscode = recordscode;
    }
}