package com.xueyi.common.web.annotation;

import java.lang.annotation.*;

/**
 * 租户控制过滤注解
 *
 * @author xueyi
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TenantIgnore {

    /** 租户控制 */
    boolean tenantLine() default false;
}