package com.config.session;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * @author MrLu
 * @createTime 2020/4/27
 * @describe  用户session 只存储这几个信息
 */
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class UserSession    {

    private String userRedisId;

    public String getUserRedisId() {
        return userRedisId;
    }

    public void setUserRedisId(String userRedisId) {
        this.userRedisId = userRedisId;
    }

    private String userRedisPerms;

    public String getUserRedisPerms() {
        return userRedisPerms;
    }

    public void setUserRedisPerms(String userRedisPerms) {
        this.userRedisPerms = userRedisPerms;
    }

    private Integer occupyCaseinInfoId;

    public Integer getOccupyCaseinInfoId() {
        return occupyCaseinInfoId;
    }

    public void setOccupyCaseinInfoId(Integer occupyCaseinInfoId) {
        this.occupyCaseinInfoId = occupyCaseinInfoId;
    }

    private String tempString;

    public String getTempString() {
        return tempString;
    }

    public void setTempString(String tempString) {
        this.tempString = tempString;
    }

    //登录版本
    private String loginVersion;

    public String getLoginVersion() {
        return loginVersion;
    }

    public void setLoginVersion(String loginVersion) {
        this.loginVersion = loginVersion;
    }
}
