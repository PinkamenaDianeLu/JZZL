package com.module.SystemManagement.Services.Impl;

import com.bean.jzgl.Converter.SysUserMapper;
import com.bean.jzgl.Source.SysUser;
import com.mapper.jzgl.SysUserDTOMapper;
import com.module.SystemManagement.Services.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author MrLu
 * @createTime 2020/9/29 13:57
 * @describe UserService 实现,数据通过数据库获取
 */
@Service("UserService")
public class UserServiceImpl  implements UserService {
    @Resource
    SysUserDTOMapper sysUserDTOMapper;
    //此时的vkey应当是用户id
    @Override
    public SysUser getUserNow(String VKey) throws Exception {
        System.err.println("空实现！");
        return null;
    }

    @Override
    public void touchUserNow( int s) throws Exception {
        System.err.println("空实现！");
    }

    @Override
    public SysUser loginVerification(String username, String pwd) throws Exception {
        return SysUserMapper.INSTANCE.pcDTOToPc(sysUserDTOMapper.login(username,pwd));
    }
}
