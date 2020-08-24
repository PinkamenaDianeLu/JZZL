package com.log;

/**
 * @author MrLu
 * @createTime 2020/8/24 15:01
 * @describe
 */

import java.lang.annotation.*;

@Target(ElementType.METHOD) //注解放置的目标位置,METHOD是可注解在方法级别上
@Retention(RetentionPolicy.RUNTIME) //注解在哪个阶段执行
@Documented
public @interface OperWebSocket {
    String operModul() default ""; // 操作模块
    enum type{SELECT,INSERT,UPDATE} ;  // 操作类型
    String operDesc() default "";  // 操作说明
    OperLog.type operType() default OperLog.type.SELECT;
}
