package com.module.SystemManagement.Controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.config.aop.OperLog;
import com.config.session.UserSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    RedisTemplate<String, Serializable> redisSerializableTemplate;

    /**
     * 登录
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
        try {
            //用户信息
            JSONObject userMessageJsonObj = (JSONObject) JSON.parse(loginMessage);


            //用户名
            String username =userMessageJsonObj.getString("oriName");
            //key
            String key =userMessageJsonObj.getString("key");
            //密码
            String oriPwd =userMessageJsonObj.getString("oriPwd");

            if (StringUtils.isEmpty(username)||StringUtils.isEmpty(key)||StringUtils.isEmpty(oriPwd)){
                throw new Exception("你传nm呢？弟弟？");
            }
            //TODO MrLu 2020/8/18 16:39 验证登录 使用用户名密码生成key与传来的key比较
            final String UserRedisId = UUID.randomUUID().toString();
            final String UserRedisPerms = UUID.randomUUID().toString();//redis key
            userSession.setUserRedisId(UserRedisId);
            userSession.setUserRedisPerms(UserRedisPerms);
            //上缴redis一个序列化的
            //redisSerializableTemplate.opsForValue().set(UserRedisId, usernow);
            //TODO MrLu 2020/8/19 9:56  用户信息上缴redis
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }

}
