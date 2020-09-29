package com.module.SystemManagement.Services;


import com.bean.jzgl.Source.SysUser;

/**
 * @Author MrLu
 * @createTime 2020/4/26 0:02
 * @describe 用户操作
 */
public interface UserService {
    /**
     * @param VKey 验证用字符串
     * @Author MrLu
     * @createTime 2020/4/26 0:13
     * @describe 获取当前用户信息
     * @version 1.0
     */
    SysUser getUserNow(String VKey) throws Exception;


     /**
     * 登录验证
     * @author MrLu
     * @param username 用户名（明文）
     * @param pwd 密码
     * @createTime  2020/9/29 11:29
     * @return    |
      */
    SysUser loginVerification(String username, String pwd) throws Exception;

}
