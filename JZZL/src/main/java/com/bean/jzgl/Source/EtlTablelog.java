package com.bean.jzgl.Source;

import java.util.Date;

public class EtlTablelog {
    private Integer id;

    private Integer scbj;

    private Integer state;

    private Date createtime;

    private Date updatetime;

    private String systemname;

    private String tablename;

    private String lastpkname;

    private Integer lastpknumvalue;

    private String lastpkstrvalue;

    private Date lastpkdatevalue;

    private Integer etltype;

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

    public String getSystemname() {
        return systemname;
    }

    public void setSystemname(String systemname) {
        this.systemname = systemname == null ? null : systemname.trim();
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename == null ? null : tablename.trim();
    }

    public String getLastpkname() {
        return lastpkname;
    }

    public void setLastpkname(String lastpkname) {
        this.lastpkname = lastpkname == null ? null : lastpkname.trim();
    }

    public Integer getLastpknumvalue() {
        return lastpknumvalue;
    }

    public void setLastpknumvalue(Integer lastpknumvalue) {
        this.lastpknumvalue = lastpknumvalue;
    }

    public String getLastpkstrvalue() {
        return lastpkstrvalue;
    }

    public void setLastpkstrvalue(String lastpkstrvalue) {
        this.lastpkstrvalue = lastpkstrvalue == null ? null : lastpkstrvalue.trim();
    }

    public Date getLastpkdatevalue() {
        return lastpkdatevalue;
    }

    public void setLastpkdatevalue(Date lastpkdatevalue) {
        this.lastpkdatevalue = lastpkdatevalue;
    }

    public Integer getEtltype() {
        return etltype;
    }

    public void setEtltype(Integer etltype) {
        this.etltype = etltype;
    }
}