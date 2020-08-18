package com.bean.jzgl;

import java.util.Date;

public class SysLogsJzchnage {
    private Integer id;

    private Integer scbj;

    private Integer state;

    private Date createtime;

    private Date updatetime;

    private String author;

    private Integer authortype;

    private String ecn;

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

    public Integer getAuthortype() {
        return authortype;
    }

    public void setAuthortype(Integer authortype) {
        this.authortype = authortype;
    }

    public String getEcn() {
        return ecn;
    }

    public void setEcn(String ecn) {
        this.ecn = ecn == null ? null : ecn.trim();
    }
}