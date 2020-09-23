package com.bean.zfba.Source;

import java.math.BigDecimal;

public class XtJcyAzSjdzb {
    private BigDecimal id;

    private BigDecimal ssjdid;

    private String jcyjdmc;

    private String azzdmc;

    private String azzdlyb;

    private Short scbj;

    private BigDecimal xh;
    
    private String defua;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getSsjdid() {
        return ssjdid;
    }

    public void setSsjdid(BigDecimal ssjdid) {
        this.ssjdid = ssjdid;
    }

    public String getJcyjdmc() {
        return jcyjdmc;
    }

    public void setJcyjdmc(String jcyjdmc) {
        this.jcyjdmc = jcyjdmc == null ? null : jcyjdmc.trim();
    }

    public String getAzzdmc() {
        return azzdmc;
    }

    public void setAzzdmc(String azzdmc) {
        this.azzdmc = azzdmc == null ? null : azzdmc.trim();
    }

    public String getAzzdlyb() {
        return azzdlyb;
    }

    public void setAzzdlyb(String azzdlyb) {
        this.azzdlyb = azzdlyb == null ? null : azzdlyb.trim();
    }

    public Short getScbj() {
        return scbj;
    }

    public void setScbj(Short scbj) {
        this.scbj = scbj;
    }

    public BigDecimal getXh() {
        return xh;
    }

    public void setXh(BigDecimal xh) {
        this.xh = xh;
    }

	public String getDefua() {
		return defua;
	}

	public void setDefua(String defua) {
		this.defua = defua == null ? null : defua.trim();
	}
    
}