package com.config.annotations;

import java.lang.annotation.*;

/**
 * @author MrLu
 * @createTime 2020/12/29 11:01
 * @describe
 */
@Target(ElementType.METHOD) //注解放置的目标位置,METHOD是可注解在方法级别上
@Retention(RetentionPolicy.RUNTIME) //注解在哪个阶段执行
@Documented
public @interface recordTidy {
    boolean updateDate() default true; // 是否续命
}
