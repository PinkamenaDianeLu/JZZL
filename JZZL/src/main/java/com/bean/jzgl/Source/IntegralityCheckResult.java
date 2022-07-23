package com.bean.jzgl.Source;


//完整性校验结果
public class IntegralityCheckResult {
    private boolean isOk;
    private StringBuilder message;


    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }

    public StringBuilder getMessage() {
        return message;
    }

    public void setMessage(StringBuilder message) {
        this.message = message;
    }
}
