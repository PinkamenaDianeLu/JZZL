package com.bean.jzgl.Source;

import java.io.Serializable;
import java.util.Date;
 /**
 * 非数据库表
 * @author MrLu
 * @createTime  2020/8/18 15:41
  */
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;


    @Override
    public String toString() {
        return "SysUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", idcardnumber='" + idcardnumber + '\'' +
                ", password='" + password + '\'' +
                ", state=" + state +
                ", createtime=" + createtime +
                ", updatetime=" + updatetime +
                ", scbj=" + scbj +
                ", xm='" + xm + '\'' +
                ", agencyid=" + agencyid +
                ", agencyname='" + agencyname + '\'' +
                ", agencycode='" + agencycode + '\'' +
                ", staffid=" + staffid +
                '}';
    }

    public SysUser() {
        super();
    }

    public SysUser(Integer id, String username, String phonenumber, String idcardnumber, String password, int state, Date createtime, Date updatetime, int scbj, String xm, Integer agencyid, String agencyname, String agencycode, int staffid) {
        this.id = id;
        this.username = username;
        this.phonenumber = phonenumber;
        this.idcardnumber = idcardnumber;
        this.password = password;
        this.state = state;
        this.createtime = createtime;
        this.updatetime = updatetime;
        this.scbj = scbj;
        this.xm = xm;
        this.agencyid = agencyid;
        this.agencyname = agencyname;
        this.agencycode = agencycode;
        this.staffid = staffid;
    }

    private Integer id;

    private String username;

    private String phonenumber;

    private String idcardnumber;

    private String password;

    private int state;

    private Date createtime;

    private Date updatetime;

    private int scbj;

    private String xm;

    private Integer agencyid;

    private String agencyname;

    private String agencycode;

    private int staffid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber == null ? null : phonenumber.trim();
    }

    public String getIdcardnumber() {
        return idcardnumber;
    }

    public void setIdcardnumber(String idcardnumber) {
        this.idcardnumber = idcardnumber == null ? null : idcardnumber.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
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

    public int getScbj() {
        return scbj;
    }

    public void setScbj(int scbj) {
        this.scbj = scbj;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm == null ? null : xm.trim();
    }

    public Integer getAgencyid() {
        return agencyid;
    }

    public void setAgencyid(Integer agencyid) {
        this.agencyid = agencyid;
    }

    public String getAgencyname() {
        return agencyname;
    }

    public void setAgencyname(String agencyname) {
        this.agencyname = agencyname == null ? null : agencyname.trim();
    }

    public String getAgencycode() {
        return agencycode;
    }

    public void setAgencycode(String agencycode) {
        this.agencycode = agencycode == null ? null : agencycode.trim();
    }

    public int getStaffid() {
        return staffid;
    }

    public void setStaffid(int staffid) {
        this.staffid = staffid;
    }
}