package com.config.aop;

import com.alibaba.fastjson.JSON;
import com.bean.jzgl.DTO.SysLogsDTO;
import com.bean.jzgl.Source.SysUser;
import com.config.annotations.OperLog;
import com.config.session.UserSession;
import com.module.SystemManagement.Services.LogService;
import com.module.SystemManagement.Services.UserService;
import com.util.IpUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author MrLu
 * @createTime 2020/4/26 22:53
 * @describe Aop切面 用于制作日志
 */
@Aspect
@Component
public class LogAspect {
    @Autowired
    LogService logService;
    @Autowired
    @Qualifier("UserServiceByRedis")
    UserService userServiceByRedis;
    @Autowired
    UserSession userSession;

    /**
     * @author MrLu
     * @createTime 2020/4/26 23:01
     * @describe 定义切入点
     * @version 1.0
     */
//    @Pointcut("execution(* com.module.*.*.*.*(..))")
    @Pointcut("@annotation(com.config.annotations.OperLog)")
    public void operLogPoint() {
    }

    /**
     * @param jp      JoinPoint
     * @param reValue 方法返回值
     * @author MrLu
     * @createTime 2020/4/26 23:27
     * @describe 切入执行后 reValue为方法的返回值
     * @version 1.0
     */
    @AfterReturning(value = "operLogPoint()", returning = "reValue")
    public void afterReturning(JoinPoint jp, Object reValue) {
        try {
            //获得request
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();

            SysLogsDTO record = new SysLogsDTO();
// 请求的参数
            Map<String, String> rtnMap = converMap(request.getParameterMap());
            // 将参数所在的数组转换成json
            String params = JSON.toJSONString(rtnMap);

            MethodSignature signature = (MethodSignature) jp.getSignature();
            Method method = signature.getMethod();
            OperLog opLog = method.getAnnotation(OperLog.class);
//            System.out.println(params);//参数 Json类型
//            System.out.println(request.getRequestURL().toString());//请求路径
//            System.out.println(jp.getSignature().getDeclaringTypeName() + jp.getSignature().getName() + "方法执行结束,返回值为：" + reValue);
//            System.out.println(opLog.operDesc() + "||" + opLog.operModul() + "||" + opLog.operType());
            if (params.length()>1998){
                record.setMparams(params.substring(0,1998));//方法返回值
            }else {
                record.setMparams(params);
            }

            record.setMparams(params);//参数Json格式
//            record.setScbj(Enums.scbj.ALIVE);
            record.setRequesturl(request.getRequestURL().toString());//请求路径
            record.setMname(jp.getSignature().getDeclaringTypeName() + jp.getSignature().getName());//方法名 全路径名
            String reVString=null==reValue?"无返回值":reValue.toString();
            if (reVString.length()>1998){
                record.setMresult(reVString.substring(0,1998));//方法返回值
            }else {
                record.setMresult(reVString);
            }

            record.setOperdesc(opLog.operDesc());//描述
            record.setOpermodul(opLog.operModul());//模块
            record.setOpertype(opLog.operType().toString());//操作类型，枚举项
            record.setIp(IpUtil.getIpAddress(request));
            SysUser userNow = userServiceByRedis.getUserNow(null);
            //判断登录状态
            if (null!=userNow){
               //已经登录
                record.setSysuserid(userNow.getId());//用户id
                record.setOperator(userNow.getIdcardnumber());
                record.setSysusername(userNow.getUsername());
            }else {
                //未登录的用户
                record.setSysuserid(-1);//用户id
                record.setOperator("未登录");
                record.setSysusername("未登录");
            }

            logService.insertLog(record);
        } catch (Exception e) {
            System.err.println("Aop日志出现了大问题！");
            e.printStackTrace();
        }

    }

    /**
     * @param jp JoinPoint
     * @param e  抛出的异常
     * @author MrLu
     * @createTime 2020/4/26 23:28
     * @describe 被切入点异常日志
     * @version 1.0
     */
    @AfterThrowing(pointcut = "operLogPoint()", throwing = "e")
    @RequestMapping
    public String saveExceptionLog(JoinPoint jp, Throwable e) {
        return "/error/FacingTheEnemy.html";
    }

    /**
     * @param paramMap request获取的参数数组
     * @author MrLu
     * @createTime 2020/4/27 0:00
     * @describe 转换request 请求参数
     * @version 1.0
     */
    public Map<String, String> converMap(Map<String, String[]> paramMap) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        for (String key : paramMap.keySet()) {
            rtnMap.put(key, paramMap.get(key)[0]);
        }
        return rtnMap;
    }


}
