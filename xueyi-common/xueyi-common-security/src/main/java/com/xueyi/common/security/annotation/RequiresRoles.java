package com.xueyi.common.security.annotation;

import java.lang.annotation.*;

/**
 * 角色认证：必须具有指定角色标识才能进入该方法
 *
 * @author xueyi
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface RequiresRoles {

    /**
     * 需要校验的角色标识
     */
    String[] value() default {};

    /**
     * 验证逻辑：AND | OR，默认AND
     */
    Logical logical() default Logical.AND;
}
