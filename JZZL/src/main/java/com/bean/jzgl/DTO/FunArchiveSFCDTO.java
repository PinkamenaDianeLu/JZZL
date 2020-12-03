package com.bean.jzgl.DTO;

import java.util.Date;

public class FunArchiveSFCDTO {
    private Integer id;

    private Integer scbj;

    private Integer state;

    private Date createtime;

    private Date updatetime;

    private String jqbh;

    private String ajbh;

    private Integer issend;
    private Integer issuspectorder;

    private String author;

    private String authoridcard;

    private Integer peoplecaseid;

    private String sfcnumber;

    private Integer archivetype;

    private String archivename;

    private Integer caseinfoid;

    private  Integer baserecordid;

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

    public Integer getIssend() {
        return issend;
    }

    public void setIssend(Integer issend) {
        this.issend = issend;
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


    public Integer getPeoplecaseid() {
        return peoplecaseid;
    }

    public void setPeoplecaseid(Integer peoplecaseid) {
        this.peoplecaseid = peoplecaseid;
    }

    public String getSfcnumber() {
        return sfcnumber;
    }

    public void setSfcnumber(String sfcnumber) {
        this.sfcnumber = sfcnumber == null ? null : sfcnumber.trim();
    }

    public Integer getArchivetype() {
        return archivetype;
    }

    public void setArchivetype(Integer archivetype) {
        this.archivetype = archivetype;
    }

    public String getArchivename() {
        return archivename;
    }

    public void setArchivename(String archivename) {
        this.archivename = archivename == null ? null : archivename.trim();
    }

    public Integer getCaseinfoid() {
        return caseinfoid;
    }

    public void setCaseinfoid(Integer caseinfoid) {
        this.caseinfoid = caseinfoid;
    }

    public Integer getBaserecordid() {
        return baserecordid;
    }

    public void setBaserecordid(Integer baserecordid) {
        this.baserecordid = baserecordid;
    }

    public Integer getIssuspectorder() {
        return issuspectorder;
    }

    public void setIssuspectorder(Integer issuspectorder) {
        this.issuspectorder = issuspectorder;
    }
}