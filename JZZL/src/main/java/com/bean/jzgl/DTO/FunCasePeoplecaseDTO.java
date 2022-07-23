package com.bean.jzgl.DTO;

import java.util.Date;

public class FunCasePeoplecaseDTO {
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

    private String sfcnumber;

    private String casename;

    private Integer caseinfoid;

    private Integer casetype;

    private Integer sysuserid;

    private String barxm;

    private Integer barsysuserid;

    private String baridcard;

    private String badwdwdm;

    private String badwdwmc;


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
        this.sfcnumber = sfcnumber == null ? null : sfcnumber.trim();
    }

    public String getCasename() {
        return casename;
    }

    public void setCasename(String casename) {
        this.casename = casename == null ? null : casename.trim();
    }

    public Integer getCaseinfoid() {
        return caseinfoid;
    }

    public void setCaseinfoid(Integer caseinfoid) {
        this.caseinfoid = caseinfoid;
    }

    public Integer getSysuserid() {
        return sysuserid;
    }

    public void setSysuserid(Integer sysuserid) {
        this.sysuserid = sysuserid;
    }

    public String getBarxm() {
        return barxm;
    }

    public void setBarxm(String barxm) {
        this.barxm = barxm == null ? null : barxm.trim();
    }

    public Integer getBarsysuserid() {
        return barsysuserid;
    }

    public void setBarsysuserid(Integer barsysuserid) {
        this.barsysuserid = barsysuserid;
    }

    public String getBaridcard() {
        return baridcard;
    }

    public void setBaridcard(String baridcard) {
        this.baridcard = baridcard == null ? null : baridcard.trim();
    }

    public String getBadwdwdm() {
        return badwdwdm;
    }

    public void setBadwdwdm(String badwdwdm) {
        this.badwdwdm = badwdwdm == null ? null : badwdwdm.trim();
    }

    public String getBadwdwmc() {
        return badwdwmc;
    }

    public void setBadwdwmc(String badwdwmc) {
        this.badwdwmc = badwdwmc == null ? null : badwdwmc.trim();
    }

    public Integer getCasetype() {
        return casetype;
    }

    public void setCasetype(Integer casetype) {
        this.casetype = casetype;
    }
}