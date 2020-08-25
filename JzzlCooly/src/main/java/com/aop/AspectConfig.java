package com.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author MrLu
 * @createTime 2020/8/24 14:49
 * @describe Aspect配置
 */
@Aspect
public class AspectConfig extends LogsRecorder {
     /**
     * 定义切入点
     * @author MrLu
     * @param 
     * @createTime  2020/8/24 14:53
     * @return    |  
      */
    @Pointcut("@annotation(com.log.OperLog)")
    public void operLogPoint() {
    }

    @AfterReturning(value = "operLogPoint()", returning = "reValue")
    public void afterReturning(JoinPoint jp, Object reValue) {
        try {
            //获得request
            String reVString=null==reValue?"无返回值":reValue.toString();
            System.out.println(reVString);

        } catch (Exception e) {
            System.err.println("Aop日志出现了大问题！");
            e.printStackTrace();
        }

    }

}
