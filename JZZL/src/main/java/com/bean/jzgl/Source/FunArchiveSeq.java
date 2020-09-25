package com.bean.jzgl.Source;

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

    private Enums.IsSend issend;

    private String author;

    private String authoridcard;

    private Integer batchesseq;

    private Enums.IsFinal isfinal;

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

    public Enums.IsSend getIssend() {
        return issend;
    }

    public void setIssend(Enums.IsSend issend) {
        this.issend = issend;
    }

    public Enums.IsFinal getIsfinal() {
        return isfinal;
    }

    public void setIsfinal(Enums.IsFinal isfinal) {
        this.isfinal = isfinal;
    }
}