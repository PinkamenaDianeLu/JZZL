package com.bean.jzgl.DTO;

import java.util.Date;

public class FunPeopelCaseDTO {
    private Integer id;

    private Integer scbj;

    private Integer state;

    private Date createtime;

    private Date updatetime;

    private String idcard;

    private String name;

    private String jqbh;

    private String ajbh;

    private Integer persontype;

    private String casename;

    private String sfcnumber;

    private Integer casestate;

    private Integer casetype;

    private Integer casestage;
    private String caseclass;
    private String caseclasscn;

    public String getCasename() {
        return casename;
    }

    public void setCasename(String casename) {
        this.casename = casename;
    }

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

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard == null ? null : idcard.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
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

    public Integer getPersontype() {
        return persontype;
    }

    public void setPersontype(Integer persontype) {
        this.persontype = persontype;
    }

    public String getSfcnumber() {
        return sfcnumber;
    }

    public void setSfcnumber(String sfcnumber) {
        this.sfcnumber = sfcnumber;
    }

    public Integer getCasestate() {
        return casestate;
    }

    public void setCasestate(Integer casestate) {
        this.casestate = casestate;
    }

    public Integer getCasetype() {
        return casetype;
    }

    public void setCasetype(Integer casetype) {
        this.casetype = casetype;
    }

    public Integer getCasestage() {
        return casestage;
    }

    public void setCasestage(Integer casestage) {
        this.casestage = casestage;
    }

    public String getCaseclass() {
        return caseclass;
    }

    public void setCaseclass(String caseclass) {
        this.caseclass = caseclass;
    }

    public String getCaseclasscn() {
        return caseclasscn;
    }

    public void setCaseclasscn(String caseclasscn) {
        this.caseclasscn = caseclasscn;
    }
}