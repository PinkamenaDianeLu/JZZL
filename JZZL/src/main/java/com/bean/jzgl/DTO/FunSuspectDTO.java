package com.bean.jzgl.DTO;

import java.util.Date;

public class FunSuspectDTO {
    private Integer id;

    private Integer scbj;

    private Integer state;

    private Date createtime;

    private Date updatetime;

    private String suspectidcard;

    private String suspectname;

    private String jqbh;

    private String ajbh;

    private Integer caseinfoid;

    private String sfcnumber;

    private Integer casetype;

    private Integer suspectstate;

    private String suspectcode;

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

    public String getSuspectidcard() {
        return suspectidcard;
    }

    public void setSuspectidcard(String suspectidcard) {
        this.suspectidcard = suspectidcard == null ? null : suspectidcard.trim();
    }

    public String getSuspectname() {
        return suspectname;
    }

    public void setSuspectname(String suspectname) {
        this.suspectname = suspectname == null ? null : suspectname.trim();
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

    public Integer getCaseinfoid() {
        return caseinfoid;
    }

    public void setCaseinfoid(Integer caseinfoid) {
        this.caseinfoid = caseinfoid;
    }

    public String getSfcnumber() {
        return sfcnumber;
    }

    public void setSfcnumber(String sfcnumber) {
        this.sfcnumber = sfcnumber == null ? null : sfcnumber.trim();
    }

    public Integer getCasetype() {
        return casetype;
    }

    public void setCasetype(Integer casetype) {
        this.casetype = casetype;
    }

    public Integer getSuspectstate() {
        return suspectstate;
    }

    public void setSuspectstate(Integer suspectstate) {
        this.suspectstate = suspectstate;
    }


    public Integer getDefaultorder() {
        return defaultorder;
    }

    public void setDefaultorder(Integer defaultorder) {
        this.defaultorder = defaultorder;
    }

    public String getSuspectcode() {
        return suspectcode;
    }

    public void setSuspectcode(String suspectcode) {
        this.suspectcode = suspectcode;
    }
}