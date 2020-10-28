package com.bean.jzgl.DTO;

import java.util.Date;

public class FunArchiveRecordindexDTO {
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

    private String indexinfo;

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

    public String getIndexinfo() {
        return indexinfo;
    }

    public void setIndexinfo(String indexinfo) {
        this.indexinfo = indexinfo == null ? null : indexinfo.trim();
    }
}