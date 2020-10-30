package com.config.annotations;

/**
 * @Author MrLu
 * @createTime 2020/4/26 23:14
 * @describe  定义日志的注解
 */
import java.lang.annotation.*;

/**
 * 各个不同模块的填写的描述（operDesc）不做特别约束，operModul是当前所属model文件夹下对应模块名
 * 如：人员档案在webApp的路径为  webApp/model/ryda   所以注解为：
 * @OperLog(operModul = "ryda",operDesc = "人员档案首页",operType = OperLog.type.SELECT)
 * 首页功能的operModul皆填写index
 *
 * */
@Target(ElementType.METHOD) //注解放置的目标位置,METHOD是可注解在方法级别上
@Retention(RetentionPolicy.RUNTIME) //注解在哪个阶段执行
@Documented
public @interface OperLog {
    String operModul() default ""; // 操作模块
    enum type{SELECT,INSERT,UPDATE,InsertOrUpdate} ;  // 操作类型
    String operDesc() default "";  // 操作说明
    type operType() default type.SELECT;


}
