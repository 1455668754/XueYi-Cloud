package com.xueyi.common.datascope.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据权限过滤注解
 *
 * @author xueyi
 * @originalAuthor ruoyi
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope
{
    /**
     * 登录验证
     */
    public String loginEnterpriseAlias() default "";

    /**
     * 租户表的别名
     */
    public String enterpriseAlias() default "";

    /**
     * 系统基础表的别名
     */
    public String systemAlias() default "";

    /**
     * 租户更新控制的别名 i 无前缀更新|other 有前缀更新
     */
    public String updateEnterpriseAlias() default "";

    /**
     * 部门表的别名
     */
    public String deptAlias() default "";

    /**
     * 岗位表的别名
     */
    public String postAlias() default "";

    /**
     * 用户表的别名
     */
    public String userAlias() default "";
}
