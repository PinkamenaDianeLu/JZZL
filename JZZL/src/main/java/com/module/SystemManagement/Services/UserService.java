package com.module.SystemManagement.Services;


import com.bean.jzgl.SysUser;

/**
 * @Author MrLu
 * @createTime 2020/4/26 0:02
 * @describe 用户操作
 */

public interface UserService {
    /**
     * @param
     * @Author MrLu
     * @createTime 2020/4/26 0:13
     * @describe 获取当前用户信息
     * @version 1.0
     */
    SysUser getUserNow() throws Exception;

}
