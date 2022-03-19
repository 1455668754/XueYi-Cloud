package com.xueyi.common.security.annotation;

import java.lang.annotation.*;

/**
 * 权限认证：必须具有指定权限才能进入该方法
 *
 * @author xueyi
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface RequiresPermissions {

    /**
     * 需要校验的权限码
     */
    String[] value() default {};

    /**
     * 验证模式：AND | OR，默认AND
     */
    Logical logical() default Logical.AND;
}