package com.config.annotations;

import java.lang.annotation.*;

/**
 * @author Mrlu
 * @createTime 2020/10/3
 * @describe  用于定义需要经过码表映射的字段
 */
@Target(ElementType.FIELD) //注解放置的目标位置,TYPE是可注解在类级别上
@Retention(RetentionPolicy.RUNTIME) //注解在哪个阶段执行
@Documented
public @interface CodeTableMapper {
    String sourceFiled() default "";  // 映射后的字段
    String codeTableType() default ""; //码表类型
}
