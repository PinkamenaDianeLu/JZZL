package com.bean.jzgl;

import java.util.Date;

public class FunArchiveType {
    private Integer id;

    private Integer scbj;

    private Integer state;

    private Date createtime;

    private Date updatetime;

    private String jqbh;

    private String ajbh;

    private Integer archivetype;

    private String archivetypecn;

    private Integer defaultorder;

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

    public Integer getArchivetype() {
        return archivetype;
    }

    public void setArchivetype(Integer archivetype) {
        this.archivetype = archivetype;
    }

    public String getArchivetypecn() {
        return archivetypecn;
    }

    public void setArchivetypecn(String archivetypecn) {
        this.archivetypecn = archivetypecn == null ? null : archivetypecn.trim();
    }

    public Integer getDefaultorder() {
        return defaultorder;
    }

    public void setDefaultorder(Integer defaultorder) {
        this.defaultorder = defaultorder;
    }
}