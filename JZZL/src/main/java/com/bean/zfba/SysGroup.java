package com.bean.zfba;


public class SysGroup {
    private Integer id;

    private String dwmc;

    private String dwmcdm;

    private String pid;

    private String dwjs;
    
    private String dwmcjp;
    
    private String sjdwdm;
    
    private String dwjc;
    
    private String g;
    
    private String j;

    private String jzdm;
    
    private String jzzw;
    
    private String state;

    public String getDwmcjp() {
		return dwmcjp;
	}

	public void setDwmcjp(String dwmcjp) {
		this.dwmcjp = dwmcjp;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDwmc() {
        return dwmc;
    }

    public void setDwmc(String dwmc) {
        this.dwmc = dwmc == null ? null : dwmc.trim();
    }

    public String getDwmcdm() {
        return dwmcdm;
    }

    public void setDwmcdm(String dwmcdm) {
        this.dwmcdm = dwmcdm == null ? null : dwmcdm.trim();
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid == null ? null : pid.trim();
    }

    public String getDwjs() {
        return dwjs;
    }

    public void setDwjs(String dwjs) {
        this.dwjs = dwjs == null ? null : dwjs.trim();
    }

	public String getSjdwdm() {
		return sjdwdm;
	}

	public void setSjdwdm(String sjdwdm) {
		this.sjdwdm = sjdwdm == null ? null : sjdwdm.trim();
	}

	public String getDwjc() {
		return dwjc;
	}

	public void setDwjc(String dwjc) {
		this.dwjc = dwjc == null ? null : dwjc.trim();
	}

	public String getG() {
		return g;
	}

	public void setG(String g) {
		this.g = g;
	}

	public String getJ() {
		return j;
	}

	public void setJ(String j) {
		this.j = j;
	}

	public String getJzdm() {
		return jzdm;
	}

	public void setJzdm(String jzdm) {
		this.jzdm = jzdm;
	}

	public String getJzzw() {
		return jzzw;
	}

	public void setJzzw(String jzzw) {
		this.jzzw = jzzw;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	
    
}