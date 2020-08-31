package com.bean.zfba.Source;

import java.math.BigDecimal;

public class XtJcyXMLJdpzb {
    private BigDecimal id;

    private BigDecimal fjdid;

    private String fjdmc;

    private String zjdmc;

    private Short sfdtsj;

    private String sfbhsjj;

    private String ssywjd;

    private BigDecimal xh;
    
    private String countsql;
    
    private String countsqlwhere;
    
    private String sjsql;
    
    private String sjsqlwhere;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getFjdid() {
        return fjdid;
    }

    public void setFjdid(BigDecimal fjdid) {
        this.fjdid = fjdid;
    }

    public String getFjdmc() {
        return fjdmc;
    }

    public void setFjdmc(String fjdmc) {
        this.fjdmc = fjdmc == null ? null : fjdmc.trim();
    }

    public String getZjdmc() {
        return zjdmc;
    }

    public void setZjdmc(String zjdmc) {
        this.zjdmc = zjdmc == null ? null : zjdmc.trim();
    }

    public Short getSfdtsj() {
        return sfdtsj;
    }

    public void setSfdtsj(Short sfdtsj) {
        this.sfdtsj = sfdtsj;
    }

    public String getSfbhsjj() {
        return sfbhsjj;
    }

    public void setSfbhsjj(String sfbhsjj) {
        this.sfbhsjj = sfbhsjj == null ? null : sfbhsjj.trim();
    }

    public String getSsywjd() {
        return ssywjd;
    }

    public void setSsywjd(String ssywjd) {
        this.ssywjd = ssywjd == null ? null : ssywjd.trim();
    }

    public BigDecimal getXh() {
        return xh;
    }

    public void setXh(BigDecimal xh) {
        this.xh = xh;
    }

	public String getCountsql() {
		return countsql;
	}

	public void setCountsql(String countsql) {
		this.countsql = countsql == null ? null : countsql.trim();
	}

	public String getCountsqlwhere() {
		return countsqlwhere;
	}

	public void setCountsqlwhere(String countsqlwhere) {
		this.countsqlwhere = countsqlwhere;
	}

	public String getSjsql() {
		return sjsql;
	}

	public void setSjsql(String sjsql) {
		this.sjsql = sjsql;
	}

	public String getSjsqlwhere() {
		return sjsqlwhere;
	}

	public void setSjsqlwhere(String sjsqlwhere) {
		this.sjsqlwhere = sjsqlwhere;
	}
	
	
	
    
}