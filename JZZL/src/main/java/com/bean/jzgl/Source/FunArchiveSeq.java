package com.bean.jzgl.Source;

import com.config.annotations.CodeTableMapper;
import com.enums.Enums;

import java.util.Date;

public class FunArchiveSeq {
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

    private Enums.IsFinal isfinal;


    private  String recordsnumber;
    private  int caseinfoid;
    private int archivetype;
    @CodeTableMapper(sourceFiled = "archivetype", codeTableType = "archivetype")
    private String archivetype_name;
    private String archivename;
    private int archivesfcid;
    private int authorid;

    private int previd;
    private Integer baserecordid;
    private int isactive;
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


    public String getIsfinal() {
        return isfinal.getName();
    }

    public void setIsfinal(Enums.IsFinal isfinal) {
        this.isfinal = isfinal;
    }

    public String getRecordsnumber() {
        return recordsnumber;
    }

    public void setRecordsnumber(String recordsnumber) {
        this.recordsnumber = recordsnumber;
    }

    public int getCaseinfoid() {
        return caseinfoid;
    }

    public void setCaseinfoid(int caseinfoid) {
        this.caseinfoid = caseinfoid;
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

    public String getArchivetype_name() {
        return archivetype_name;
    }

    public void setArchivetype_name(String archivetype_name) {
        this.archivetype_name = archivetype_name;
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

    public int getPrevid() {
        return previd;
    }

    public void setPrevid(int previd) {
        this.previd = previd;
    }

    public Integer getBaserecordid() {
        return baserecordid;
    }

    public void setBaserecordid(Integer baserecordid) {
        this.baserecordid = baserecordid;
    }

    public int getIsactive() {
        return isactive;
    }

    public void setIsactive(int isactive) {
        this.isactive = isactive;
    }
}