package com.bean.jzgl.Source;

import java.io.Serializable;
import java.util.Date;

/**
 * @author MrLu
 * @createTime 2020/12/29 10:54
 * @describe 用户方在redis记录案件被占用
 */
public class CasesAreOccupied implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer caseinfid;
    private Date occupiedTime;
    private String username;
    private Integer userid;
    private String xm;
    private Integer sfcid;
    private String sfcname;

    @Override
    public String toString() {
        return "CasesAreOccupied{" +
                "caseinfid=" + caseinfid +
                ", occupiedTime=" + occupiedTime +
                ", username='" + username + '\'' +
                ", userid=" + userid +
                ", xm='" + xm + '\'' +
                ", sfcid=" + sfcid +
                ", sfcname='" + sfcname + '\'' +
                '}';
    }

    public CasesAreOccupied(Integer caseinfid, Date occupiedTime, String username, Integer userid, String xm, Integer sfcid, String sfcname) {
        this.caseinfid = caseinfid;
        this.occupiedTime = occupiedTime;
        this.username = username;
        this.userid = userid;
        this.xm = xm;
        this.sfcid = sfcid;
        this.sfcname = sfcname;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public CasesAreOccupied() {
    }

    public Integer getSfcid() {
        return sfcid;
    }

    public void setSfcid(Integer sfcid) {
        this.sfcid = sfcid;
    }

    public String getSfcname() {
        return sfcname;
    }

    public void setSfcname(String sfcname) {
        this.sfcname = sfcname;
    }

    public Integer getCaseinfid() {
        return caseinfid;
    }

    public void setCaseinfid(Integer caseinfid) {
        this.caseinfid = caseinfid;
    }

    public Date getOccupiedTime() {
        return occupiedTime;
    }

    public void setOccupiedTime(Date occupiedTime) {
        this.occupiedTime = occupiedTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }
}
