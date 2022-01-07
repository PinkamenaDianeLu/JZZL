package com.module.SystemManagement.Controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bean.jzgl.DTO.FunCaseInfoDTO;
import com.bean.jzgl.DTO.FunCasePeoplecaseDTO;
import com.bean.jzgl.DTO.SysLogsLoginDTO;
import com.bean.jzgl.Source.SysRoleUser;
import com.bean.jzgl.Source.SysUser;
import com.config.annotations.OperLog;
import com.config.session.UserSession;
import com.enums.Enums;
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
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    private final
    RedisTemplate<String, Object> redisOnlineUserTemplate;

    private final UserService userServiceByRedis;

    public LogController(LogService logService,
                         @Qualifier("redisCSTemplate") RedisTemplate<String, Serializable> redisCSTemplate,
                         RedisTemplate<String, Object> redisCCTemplate,
                         RedisTemplate<String, Object> redisOnlineUserTemplate,
                         @Qualifier("UserService") UserService userService,
                         UserSession userSession,
                         @Qualifier("UserServiceByRedis") UserService userServiceByRedis) {
        this.logService = logService;
        this.redisCSTemplate = redisCSTemplate;
        this.redisCCTemplate = redisCCTemplate;
        this.redisOnlineUserTemplate = redisOnlineUserTemplate;
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
//            String DecodeUsername = username;
//            String DecodePwd = oriPwd;
//            String DecodeUsername= ThreeDesUtil.des3DecodeCBC(username);
//            String DecodePwd= ThreeDesUtil.des3DecodeCBC(oriPwd);

            if ("123".equals(key)) {
                //本系统自我登录
                SysUser sysUserNow = userService.loginVerification(username, oriPwd);
                if (null == sysUserNow) {
                    //用户名密码验证失败
                    reValue.put("message", "deny");
                } else {
                    //查看当前用户的权限
                    //正常登录
                    loginToRedis(sysUserNow);
                    userSession.setLoginVersion("Login");//本地登录
                }
            } else {
                //通过其它途径进入系统
                reValue = loginByUrl(username, key, oriPwd);
            }
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }

    /**
     * 其它系统的跳转登录
     *
     * @param username 用户身份证号
     * @param key      参数以及验证传递来源 base64("来源代码","警情编号","参数json")
     * @param oriPwd   密码
     * @return |
     * @author MrLu
     * @createTime 2021/4/8 15:38
     */
    private JSONObject loginByUrl(String username, String key, String oriPwd) throws Exception {
        JSONObject reValue = new JSONObject();
        reValue.put("message", "error");


        byte[] asBytes = Base64.getDecoder().decode(key);
        String realKeys = new String(asBytes);
        String paramJson = realKeys.substring(realKeys.indexOf("{"));//截取最后的参数
        String[] keys = realKeys.split(",");
        //来源
        String comeFrom = keys[0].toUpperCase();
        //http://ip:端口/username=base64的用户名&password=base64的密码$key=base64(SSHB_AZXT,jqbh)
//http://35.2.31.58:8080/?username=YWRtaW4=&password=MjAyY2I5NjJhYzU5MDc1Yjk2NGIwNzE1MmQyMzRiNzA=&key=123
        if ("SSHB_AZXT".equals(comeFrom)) {
            //案宗系统跳转
            //加密规则：password=md5(身份证号+key)
            //要访问的案件
            String jqbh = keys[1];
          /*  byte[] passwordBytes = Base64.getDecoder().decode(oriPwd);
            String realPasswordBytes = new String(passwordBytes);*/
            //判断密码是否符合规则
            if (Enums.passwordSwitch.sendToAz.getValue().contains(oriPwd)) {
                //判断该警情所属的案件的主/辅办人是否是他
                List<FunCasePeoplecaseDTO> cases = logService.selectCaseByJqIDCard(jqbh);
                if (!"ALLCASE".equals(jqbh)) {
                    //将案综传递的参数以对象方式传递至前台
                    if (StringUtils.isNotBlank(paramJson)) {
                        JSONObject azParam = JSON.parseObject(paramJson);
                        reValue.put("azParam", azParam);
                    }
                    if (cases.size() > 0) {
                        //记录登录并跳转message
                        SysUser sysUserNow = userService.loginVerification(username);
                        //记录登录日志
                        loginToRedis(sysUserNow);

                        reValue.put("message", "urlLogin_success");
                        reValue.put("value", cases.get(0).getCaseinfoid());
                        //将安综传递的部分参数保存至session
                        userSession.setTempString(paramJson);

                        userSession.setLoginVersion("SSHB_AZXT");
                    } else {

                        reValue.put("message", "NoCase");
                        reValue.put("information", jqbh);
                        //！
                    }
                } else {
                    //查询用户的全部案件 跳转至全部案件
                    userSession.setLoginVersion("SSHB_AZXT_ALLCASE");
                    //记录登录并跳转message
                    SysUser sysUserNow = userService.loginVerification(username);
                    //记录登录日志
                    loginToRedis(sysUserNow);
                    reValue.put("message", "urlLogin_success_ALLCASE");
                    reValue.put("value", "ALLCASE");
                }
            } else {
                reValue.put("message", "urlLogin_deny");
                reValue.put("information", "无法识别传递的密钥");
            }
        } else if ("DQ_ZFQLC".equals(comeFrom) || "DXAL_ZFQLC".equals(comeFrom)) {
            String jqbh = keys[1];

            //判断密码是否符合规则
            if (Enums.passwordSwitch.sendToDqZfqlc.getValue().contains(oriPwd)) {
                //判断该警情所属的案件的主/辅办人是否是他
                FunCaseInfoDTO cases = logService.selectCaseByJqID(jqbh);
                if (null != cases && null != cases.getId()) {
                    //记录登录并跳转message
                    SysUser sysUserNow = userService.loginVerification(username);
                    //记录登录日志
                    loginToRedis(sysUserNow);
                    reValue.put("message", "urlLogin_success");
                    reValue.put("value", cases.getId());
                    userSession.setLoginVersion("ZFQLC");

                } else {
                    reValue.put("message", "urlLogin_deny");
                    reValue.put("information", "案件不存在或与该用户无关");
                }
            } else {
                reValue.put("message", "urlLogin_deny");
                reValue.put("information", "无法识别传递的密钥");
            }
        } else if ("YTJ".equals(comeFrom)) {
            String jqbh = keys[1];
            //判断密码是否符合规则
            if (Enums.passwordSwitch.sendToYTJ.getValue().contains(oriPwd)) {
                //判断有没有这个案子
                FunCaseInfoDTO cases = logService.selectCaseByJqID(jqbh);
                if (null != cases && null != cases.getId()) {
                    //记录登录并跳转message
                    SysUser sysUserNow = userService.loginVerification(username);
                    //记录登录日志
                    loginToRedis(sysUserNow);
                    reValue.put("message", "urlLogin_success");
                    reValue.put("value", cases.getId());
                    userSession.setLoginVersion("YTJ");//一体机登录
                } else {
                    reValue.put("message", "urlLogin_deny");
                    reValue.put("information", "案件不存在");
                }
            } else {
                reValue.put("message", "urlLogin_deny");
                reValue.put("information", "无法识别传递的密钥");
            }
        }
        return reValue;
    }

    /**
     * 登录信息记载至redis
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/4/8 16:16
     */
    private void loginToRedis(SysUser sysUserNow) {
        //获取随机字符串作为key
        final String UserRedisId = "redisUser" + UUID.randomUUID();
        //上缴session 在redis中的对应id
        userSession.setUserRedisId(UserRedisId);
        //上缴redis一个序列化的
        redisCSTemplate.opsForValue().set(UserRedisId, sysUserNow, 900000, TimeUnit.SECONDS);
        //上交用户的权限
        //查询该用户的权限
        List<SysRoleUser> userRoles = userService.selectRoleByUserid(sysUserNow.getId());
        String roles = userRoles.stream().map(SysRoleUser::getRolecode).map(String::valueOf).collect(Collectors.joining(",", ",", ","));//将用户的权限转换成一个以，分割的字符串
        sysUserNow.setRole(roles);//记录该用户的权限
        //记录在线用户  key：身份证号   value 登录时间  持续时间600s
        redisOnlineUserTemplate.opsForValue().set(sysUserNow.getUsername(), new Date(), 900000, TimeUnit.SECONDS);

        //记录登录日志
        saveLoginLog(sysUserNow);
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
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2020/12/8 14:59
     */
    @RequestMapping(value = "/getPrevLoginHistory", method = {RequestMethod.GET,
            RequestMethod.POST}, produces = "text/html;charset=UTF-8")
    @ResponseBody
    @OperLog(operModul = "index", operDesc = "获取上次登录日志", operType = OperLog.type.SELECT)
    public String getPrevLoginHistory() {
        JSONObject reValue = new JSONObject();
        try {
            SysUser userNow = userServiceByRedis.getUserNow(null);//获取当前用户
            SysLogsLoginDTO userLogHistory=logService.selectPrevLogHistory(userNow.getId());
            if (null==userLogHistory){
                //该用户第一次登陆 没有登陆记录
                //返回用户信息
                reValue.put("value", userNow);
            }else {
                reValue.put("value",userLogHistory );
            }

            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }

    /**
     * 获取当前用户的登录方式
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/8/25 14:43
     */
    @RequestMapping(value = "/getUserLoginVersion", method = {RequestMethod.GET,
            RequestMethod.POST}, produces = "text/html;charset=UTF-8")
    @ResponseBody
    @OperLog(operModul = "index", operDesc = "获取当前用户的登录方式", operType = OperLog.type.SELECT)
    public String getUserLoginVersion() {
        JSONObject reValue = new JSONObject();
        try {
            reValue.put("value", userSession.getLoginVersion());
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }
}
