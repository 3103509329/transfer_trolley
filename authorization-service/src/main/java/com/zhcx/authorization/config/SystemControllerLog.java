package com.zhcx.authorization.config;

import java.lang.annotation.*;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2018/10/26 17:24
 **/
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemControllerLog {
    String description() default "";
    String actionType() default "" ;//1:登录系统，2：创建账号，3：修改账号，4：重置密码，5：修改密码
}