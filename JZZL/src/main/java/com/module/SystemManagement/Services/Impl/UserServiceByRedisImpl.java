package com.module.SystemManagement.Services.Impl;

import com.bean.jzgl.Source.SysUser;
import com.config.session.UserSession;
import com.module.SystemManagement.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * @Author MrLu
 * @createTime 2020/4/26 0:05
 * @describe UserService 实现,数据通过redis获取
 */
@Service("UserServiceByRedis")
public class UserServiceByRedisImpl implements UserService {

    @Autowired
    RedisTemplate<String, String> redisTemplate;
    @Autowired
    RedisTemplate<String, Serializable> redisSerializableTemplate;
    @Autowired
    RedisTemplate<String, Object> redisCLTemplate;
    @Autowired
    UserSession userSession;

    @Override
    public SysUser getUserNow(String VKey) throws Exception {
        String userUUid=userSession.getUserRedisId();
        if (null!=userUUid){
            //不为空  有用户登录
            return (SysUser) redisSerializableTemplate.opsForValue().get(userSession.getUserRedisId());
        }else {
            //没人登录 返回null
            return null;
        }
    }

    @Override
    public SysUser loginVerification(String username, String pwd) throws Exception {
        return null;
    }
}
