package com.zhcx.netcar.annotation;

import java.lang.annotation.*;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/7/9 10:20
 **/
@Retention(RetentionPolicy.RUNTIME) // 注解会在class字节码文件中存在，在运行时可以通过反射获取到
@Target({ElementType.FIELD,ElementType.METHOD})//定义注解的作用目标**作用范围字段、枚举的常量/方法
@Documented//说明该注解将被包含在javadoc中
public @interface FieldMeta {
    /**
     * 字段名称
     * @return
     */
    String name() default "";

    /**
     * 字段描述
     * @return
     */
    String description() default "";

    /**
     * 字段列索引
     * @return
     */
    int index() default 0;
}


