package com.bean.jzgl.Source;

import java.io.Serializable;
import java.util.Date;

public class SysUser implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;

    private Integer scbj;

    private Integer state;

    private Date createtime;

    private Date updatetime;

    private String username;

    private String password;

    private String origin;

    private String phone;
    private String idcardnumber;

    private String xm;
    private  String agencyname;
    private  String agencycode;

    @Override
    public String toString() {
        return "SysUser{" +
                "id=" + id +
                ", scbj=" + scbj +
                ", state=" + state +
                ", createtime=" + createtime +
                ", updatetime=" + updatetime +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", origin='" + origin + '\'' +
                ", phone='" + phone + '\'' +
                ", idcardnumber='" + idcardnumber + '\'' +
                ", xm='" + xm + '\'' +
                ", agencyname='" + agencyname + '\'' +
                ", agencycode='" + agencycode + '\'' +
                '}';
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin == null ? null : origin.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdcardnumber() {
        return idcardnumber;
    }

    public void setIdcardnumber(String idcardnumber) {
        this.idcardnumber = idcardnumber;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getAgencyname() {
        return agencyname;
    }

    public void setAgencyname(String agencyname) {
        this.agencyname = agencyname;
    }

    public String getAgencycode() {
        return agencycode;
    }

    public void setAgencycode(String agencycode) {
        this.agencycode = agencycode;
    }
}