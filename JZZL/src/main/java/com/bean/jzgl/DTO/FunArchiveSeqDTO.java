package com.bean.jzgl.DTO;

import java.util.Date;

public class FunArchiveSeqDTO {
    private Integer id;

    private Integer scbj;

    private Integer state;

    private Date createtime;

    private Date updatetime;

    private String jqbh;

    private String ajbh;

    private  String sfcnumber;

    private String author;

    private String authoridcard;

    private Integer batchesseq;

    private Integer isfinal;

    private String recordsnumber;
    private int peopelcaseid;
    private int archivetype;
    private String archivename;

    private int archivesfcid;
    private int authorid;

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


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author == null ? null : author.trim();
    }

    public String getAuthoridcard() {
        return authoridcard;
    }

    public void setAuthoridcard(String authoridcard) {
        this.authoridcard = authoridcard == null ? null : authoridcard.trim();
    }

    public Integer getBatchesseq() {
        return batchesseq;
    }

    public void setBatchesseq(Integer batchesseq) {
        this.batchesseq = batchesseq;
    }

    public Integer getIsfinal() {
        return isfinal;
    }

    public void setIsfinal(Integer isfinal) {
        this.isfinal = isfinal;
    }

    public String getRecordsnumber() {
        return recordsnumber;
    }

    public void setRecordsnumber(String recordsnumber) {
        this.recordsnumber = recordsnumber;
    }

    public int getPeopelcaseid() {
        return peopelcaseid;
    }

    public void setPeopelcaseid(int peopelcaseid) {
        this.peopelcaseid = peopelcaseid;
    }


    public int getArchivetype() {
        return archivetype;
    }

    public void setArchivetype(int archivetype) {
        this.archivetype = archivetype;
    }

    public String getArchivename() {
        return archivename;
    }

    public void setArchivename(String archivename) {
        this.archivename = archivename;
    }

    public int getArchivesfcid() {
        return archivesfcid;
    }

    public void setArchivesfcid(int archivesfcid) {
        this.archivesfcid = archivesfcid;
    }

    public String getSfcnumber() {
        return sfcnumber;
    }

    public void setSfcnumber(String sfcnumber) {
        this.sfcnumber = sfcnumber;
    }

    public int getAuthorid() {
        return authorid;
    }

    public void setAuthorid(int authorid) {
        this.authorid = authorid;
    }
}