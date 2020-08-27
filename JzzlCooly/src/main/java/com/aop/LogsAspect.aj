package com.aop;

import org.aspectj.lang.reflect.CodeSignature;

import java.util.Map;

/**
 * 用于记录日志的切面
 * @author MrLu
 * @createTime  2020/8/25 11:31
  */
public aspect LogsAspect  {
//    Object  around():call(* com.aop.test.testq(java.lang.String)){
//        System.out.println("开始事务...");
//        Object re=  proceed();
//        System.out.println("事务结束...");
//        return re;
//    }

    //定义切入点
     pointcut  runMethod():call(* com.aop.*.*(..));
     //方法执行前  没毛线用
     before(): runMethod() {
         System.out.println("about to move");
     }

     //after returning, after throwing, and plain after
     //方法执行后
     /**
      the kind of join point that was matched
      the source location of the code associated with the join point
      normal, short and long string representations of the current join point
      the actual argument values of the join point
      the signature of the member associated with the join point
      the currently executing object
      the target object
      an object encapsulating the static information about the join point. This is also available through the special variable thisJoinPointStaticPart.
     * */
     after() returning: runMethod() {

         Object[] paramValues = thisJoinPoint.getArgs();
         System.out.println(thisJoinPoint.getSourceLocation().getFileName());//运行方法的文件名
         System.out.println("Intercepted message: " +
                 thisJoinPointStaticPart.getSignature().getName());//方法名
         System.out.println("in class: " +
                 thisJoinPointStaticPart.getSignature().getDeclaringType().getName());//类名
         Object[] args = thisJoinPoint.getArgs();
         String[] names = ((CodeSignature)thisJoinPoint.getSignature()).getParameterNames();
         Class[] types = ((CodeSignature)thisJoinPoint.getSignature()).getParameterTypes();
         for (int i = 0; i < args.length; i++) {
             System.out.println("  "  + i + ". " + names[i] +//参数名
                     " : " +            types[i].getName() +//参数类型
                     " = " +            args[i]);//参数值
         }
//         Map<String, String> pList= super.getMethodParameters(thisJoinPoint);

         System.out.println("just successfully moved");
     }
     //方法抛出异常
     after() throwing: runMethod() {
         System.out.println("just successfully moved");
     }
}
