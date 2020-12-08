package com.module.SystemManagement.Controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bean.jzgl.DTO.SysLogsLoginDTO;
import com.bean.jzgl.Source.SysUser;
import com.config.annotations.OperLog;
import com.config.session.UserSession;
import com.module.SystemManagement.Services.LogService;
import com.module.SystemManagement.Services.UserService;
import com.util.IpUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
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

    private final
    UserSession userSession;

    private final
    UserService userService;

    private final
    LogService logService;

    private final
    RedisTemplate<String, Serializable> redisCSTemplate;
    private final
    RedisTemplate<String, Object> redisCCTemplate;
    private final UserService userServiceByRedis;
    public LogController(LogService logService, RedisTemplate<String, Serializable> redisCSTemplate, RedisTemplate<String, Object> redisCCTemplate, @Qualifier("UserService") UserService userService, UserSession userSession, @Qualifier("UserServiceByRedis") UserService userServiceByRedis) {
        this.logService = logService;
        this.redisCSTemplate = redisCSTemplate;
        this.redisCCTemplate = redisCCTemplate;
        this.userService = userService;
        this.userSession = userSession;
        this.userServiceByRedis = userServiceByRedis;
    }

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
                //记录登录日志
                saveLoginLog(sysUserNow);
            }

        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }

    /**
     * 记录登录日志
     *
     * @param sysUserNow 已经登录的用户
     * @return void  |
     * @author MrLu
     * @createTime 2020/10/8 15:03
     */
    private void saveLoginLog(SysUser sysUserNow) {
        logService.updateHistoryLog(sysUserNow.getId());//将之前的所有日志标记为过去
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        SysLogsLoginDTO SysLogsLoginDTO = new SysLogsLoginDTO();
        SysLogsLoginDTO.setIp(IpUtil.getIpAddress(request));
        SysLogsLoginDTO.setXm(sysUserNow.getXm());
        SysLogsLoginDTO.setOperator(sysUserNow.getIdcardnumber());
        SysLogsLoginDTO.setSysusername(sysUserNow.getUsername());
        SysLogsLoginDTO.setState(1);//这是本次登录的日志
        SysLogsLoginDTO.setSysuserid(sysUserNow.getId());
        logService.insertLogLogin(SysLogsLoginDTO);
    }

     /**
     * 获取上次登录日志
     * @author MrLu
     * @param
     * @createTime  2020/12/8 14:59
     * @return    |
      */
    @RequestMapping(value = "/getPrevLoginHistory", method = {RequestMethod.GET,
            RequestMethod.POST}, produces = "text/html;charset=UTF-8")
    @ResponseBody
    @OperLog(operModul = "index", operDesc = "获取上次登录日志", operType = OperLog.type.SELECT)
    public String getPrevLoginHistory() {
        JSONObject reValue = new JSONObject();
        try {
            SysUser userNow = userServiceByRedis.getUserNow(null);//获取当前用户
            reValue.put("value", logService.selectPrevLogHistory(userNow.getId()));
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }

}
