package com.bean.jzgl.Source;

import java.util.Date;

public class FunArchiveTags {
    private Integer id;

    private Integer scbj;

    private Integer state;

    private Date createtime;

    private Date updatetime;

    private String author;

    private Integer authorid;

    private String authorxm;

    private Integer archivesfcid;

    private Integer archiveseqid;

    private Integer archivetypeid;

    private Integer archivefileid;

    private String filecode;

    private String taginfo;

    private String tagcolour;

    private String positionx;

    private String positiony;

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

    public String getTaginfo() {
        return taginfo;
    }

    public void setTaginfo(String taginfo) {
        this.taginfo = taginfo == null ? null : taginfo.trim();
    }

    public String getTagcolour() {
        return tagcolour;
    }

    public void setTagcolour(String tagcolour) {
        this.tagcolour = tagcolour == null ? null : tagcolour.trim();
    }

    public String getPositionx() {
        return positionx;
    }

    public void setPositionx(String positionx) {
        this.positionx = positionx == null ? null : positionx.trim();
    }

    public String getPositiony() {
        return positiony;
    }

    public void setPositiony(String positiony) {
        this.positiony = positiony == null ? null : positiony.trim();
    }
}