package com.bean.jzgl.DTO;

import java.util.Date;

public class FunArchiveBackcoverDTO {
    private Integer id;

    private Integer scbj;

    private Integer state;

    private Date createtime;

    private Date updatetime;

    private String author;

    private Integer authorid;

    private String authorxm;

    private String jqbh;

    private String ajbh;

    private Integer archivesfcid;

    private Integer archiveseqid;

    private Integer archivetypeid;

    private Integer archivefileid;


    private String filecode;


    private String documentpage;

    private String scriptpage;

    private String photopage;

    private String ljsj;

    private String ljr;

    private String explain;

    private String review;

    private Integer reviewid;

    private String reviewxm;

    private String reviewdate;

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author == null ? null : author.trim();
    }

    public Integer getAuthorid() {
        return authorid;
    }

    public void setAuthorid(Integer authorid) {
        this.authorid = authorid;
    }

    public String getAuthorxm() {
        return authorxm;
    }

    public void setAuthorxm(String authorxm) {
        this.authorxm = authorxm == null ? null : authorxm.trim();
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

    public Integer getArchivesfcid() {
        return archivesfcid;
    }

    public void setArchivesfcid(Integer archivesfcid) {
        this.archivesfcid = archivesfcid;
    }

    public Integer getArchiveseqid() {
        return archiveseqid;
    }

    public void setArchiveseqid(Integer archiveseqid) {
        this.archiveseqid = archiveseqid;
    }

    public Integer getArchivetypeid() {
        return archivetypeid;
    }

    public void setArchivetypeid(Integer archivetypeid) {
        this.archivetypeid = archivetypeid;
    }

    public Integer getArchivefileid() {
        return archivefileid;
    }

    public void setArchivefileid(Integer archivefileid) {
        this.archivefileid = archivefileid;
    }


    public String getFilecode() {
        return filecode;
    }

    public void setFilecode(String filecode) {
        this.filecode = filecode == null ? null : filecode.trim();
    }


    public String getDocumentpage() {
        return documentpage;
    }

    public void setDocumentpage(String documentpage) {
        this.documentpage = documentpage == null ? null : documentpage.trim();
    }

    public String getScriptpage() {
        return scriptpage;
    }

    public void setScriptpage(String scriptpage) {
        this.scriptpage = scriptpage == null ? null : scriptpage.trim();
    }

    public String getPhotopage() {
        return photopage;
    }

    public void setPhotopage(String photopage) {
        this.photopage = photopage == null ? null : photopage.trim();
    }

    public String getLjsj() {
        return ljsj;
    }

    public void setLjsj(String ljsj) {
        this.ljsj = ljsj == null ? null : ljsj.trim();
    }

    public String getLjr() {
        return ljr;
    }

    public void setLjr(String ljr) {
        this.ljr = ljr == null ? null : ljr.trim();
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain == null ? null : explain.trim();
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review == null ? null : review.trim();
    }

    public Integer getReviewid() {
        return reviewid;
    }

    public void setReviewid(Integer reviewid) {
        this.reviewid = reviewid;
    }

    public String getReviewxm() {
        return reviewxm;
    }

    public void setReviewxm(String reviewxm) {
        this.reviewxm = reviewxm == null ? null : reviewxm.trim();
    }

    public String getReviewdate() {
        return reviewdate;
    }

    public void setReviewdate(String reviewdate) {
        this.reviewdate = reviewdate == null ? null : reviewdate.trim();
    }
}