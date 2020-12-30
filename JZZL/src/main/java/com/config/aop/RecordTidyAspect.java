package com.config.aop;

import com.alibaba.fastjson.JSON;
import com.bean.jzgl.DTO.SysLogsDTO;
import com.bean.jzgl.Source.SysUser;
import com.config.annotations.OperLog;
import com.config.annotations.recordTidy;
import com.config.session.UserSession;
import com.module.SystemManagement.Services.UserService;
import com.util.IpUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author MrLu
 * @createTime 2020/12/29 11:05
 * @describe 用于给卷整理页面续命
 */
@Aspect
@Component
public class RecordTidyAspect {
    @Autowired
    @Qualifier("redisCasesAreOccupiedTemplate")
    RedisTemplate<String, Serializable> redisCasesAreOccupiedTemplate;
    @Autowired
    UserSession userSession;

    @Pointcut("@annotation(com.config.annotations.recordTidy)")
    public void recordTidyPoint() {
    }

    @AfterReturning(value = "recordTidyPoint()", returning = "reValue")
    public void afterReturning(JoinPoint jp, Object reValue) {
        try {
            MethodSignature signature = (MethodSignature) jp.getSignature();
            Method method = signature.getMethod();
            recordTidy rt = method.getAnnotation(recordTidy.class);
            Integer caseInfoId = userSession.getOccupyCaseinInfoId();
            if (null != caseInfoId && rt.updateDate()) {
                redisCasesAreOccupiedTemplate.expire("C"+caseInfoId, 660, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            System.err.println("整理占用家世失败");
            e.printStackTrace();
        }

    }
}
