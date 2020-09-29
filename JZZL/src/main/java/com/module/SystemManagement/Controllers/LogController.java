package com.module.SystemManagement.Controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bean.jzgl.Source.SysUser;
import com.config.aop.OperLog;
import com.config.session.UserSession;
import com.module.SystemManagement.Services.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author MrLu
 * @createTime 2020/8/18
 * @describe 登录
 */
@Controller
@RequestMapping("/Login")
public class LogController {

    @Autowired
    UserSession userSession;

    @Qualifier("UserService")
    @Autowired
    UserService userService;

    @Autowired
    RedisTemplate<String, Serializable> redisCSTemplate;
    @Autowired
    RedisTemplate<String, Object> redisCCTemplate;

    /**
     * 登录
     *
     * @param loginMessage JSON类型字符串
     * @return String |
     * @author MrLu
     * @createTime 2020/8/18 16:12
     */
    @RequestMapping(value = "/login", method = {RequestMethod.GET,
            RequestMethod.POST}, produces = "text/html;charset=UTF-8")
    @ResponseBody
    @OperLog(operModul = "index", operDesc = "登录", operType = OperLog.type.SELECT)
    public String log(String loginMessage) {

        JSONObject reValue = new JSONObject();
        reValue.put("message", "success");
        try {
            //用户信息
            JSONObject userMessageJsonObj = (JSONObject) JSON.parse(loginMessage);
            //用户名
            String username = userMessageJsonObj.getString("oriName");
            //key
            String key = userMessageJsonObj.getString("key");
            //密码
            String oriPwd = userMessageJsonObj.getString("oriPwd");

            if (StringUtils.isEmpty(key)) {
                throw new Exception("你传nm呢？弟弟？");
            }
            //解密字符串
            String DecodeUsername = username;
            String DecodePwd = oriPwd;
//            String DecodeUsername= ThreeDesUtil.des3DecodeCBC(username);
//            String DecodePwd= ThreeDesUtil.des3DecodeCBC(oriPwd);
            SysUser sysUserNow = userService.loginVerification(DecodeUsername, DecodePwd);
            if (null == sysUserNow) {
                //用户名密码验证失败
                reValue.put("message", "deny");
            } else {
                //获取随机字符串作为key
                final String UserRedisId = "redisUser" + UUID.randomUUID();
                //上缴session 在redis中的对应id
                userSession.setUserRedisId(UserRedisId);
                //上缴redis一个序列化的
                redisCSTemplate.opsForValue().set(UserRedisId, sysUserNow);

            }

        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }

}
