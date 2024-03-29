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
    private String recordscode;
    private Integer isdelete;
    private Integer isazxt;
    private Integer archivesfcid;
    private String author;

    private Integer authorid;
    private Integer previd;
    private String jcyrecordcode;
    private String recordwh;
    private Date issuetime;
    private Integer baserecordid;
    private String recorduuid;
    private Integer wjbid;
    private String wjbm;
    private  Date effectivetime;
    private String authoridcard;
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

    public Integer getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(Integer isdelete) {
        this.isdelete = isdelete;
    }

    public Integer getIsazxt() {
        return isazxt;
    }

    public void setIsazxt(Integer isazxt) {
        this.isazxt = isazxt;
    }

    public Integer getArchivesfcid() {
        return archivesfcid;
    }

    public void setArchivesfcid(Integer archivesfcid) {
        this.archivesfcid = archivesfcid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getAuthorid() {
        return authorid;
    }

    public void setAuthorid(Integer authorid) {
        this.authorid = authorid;
    }

    public Integer getPrevid() {
        return previd;
    }

    public void setPrevid(Integer previd) {
        this.previd = previd;
    }

    public String getJcyrecordcode() {
        return jcyrecordcode;
    }

    public void setJcyrecordcode(String jcyrecordcode) {
        this.jcyrecordcode = jcyrecordcode;
    }

    public String getRecordwh() {
        return recordwh;
    }

    public void setRecordwh(String recordwh) {
        this.recordwh = recordwh;
    }

    public Date getIssuetime() {
        return issuetime;
    }

    public void setIssuetime(Date issuetime) {
        this.issuetime = issuetime;
    }

    public Integer getBaserecordid() {
        return baserecordid;
    }

    public void setBaserecordid(Integer baserecordid) {
        this.baserecordid = baserecordid;
    }

    public String getRecorduuid() {
        return recorduuid;
    }

    public void setRecorduuid(String recorduuid) {
        this.recorduuid = recorduuid;
    }

    public Integer getWjbid() {
        return wjbid;
    }

    public void setWjbid(Integer wjbid) {
        this.wjbid = wjbid;
    }

    public String getWjbm() {
        return wjbm;
    }

    public void setWjbm(String wjbm) {
        this.wjbm = wjbm;
    }

    public Date getEffectivetime() {
        return effectivetime;
    }

    public void setEffectivetime(Date effectivetime) {
        this.effectivetime = effectivetime;
    }

    public String getAuthoridcard() {
        return authoridcard;
    }

    public void setAuthoridcard(String authoridcard) {
        this.authoridcard = authoridcard;
    }
}