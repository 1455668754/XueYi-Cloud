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
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {
    /**
     * 登录验证
     */
    public String loginAlias() default "";

    /**
     * 租户表的别名 | 控制到 e enterpriseId
     */
    public String eAlias() default "";

    /**
     * 租户表的别名 | 控制到 e enterpriseId | s 系统Id systemId
     */
    public String SeAlias() default "";

    /**
     * 租户表的别名 | 控制到 e enterpriseId | w 站点Id siteId
     */
    public String WeAlias() default "";

    /**
     * 租户表的别名 | 控制到 e enterpriseId | s 系统Id systemId | w 站点Id siteId
     */
    public String SWeAlias() default "";

    /**
     * 租户表的别名 | 控制到 e enterpriseId | s 系统Id systemId | w 站点Id siteId | l 产品库Id libraryId
     */
    public String SWLeAlias() default "";

    /**
     * 租户表的别名 | 控制到 e enterpriseId（包含租户id=0的数据）
     */
    public String edAlias() default "";

    /**
     * 租户表的别名 | 控制到 e enterpriseId | s 系统Id systemId （包含租户id=0的数据）
     */
    public String SedAlias() default "";

    /**
     * 租户表的别名 | 控制到 e enterpriseId | w 站点Id siteId （包含租户id=0的数据）
     */
    public String WedAlias() default "";

    /**
     * 租户表的别名 | 控制到 e enterpriseId | s 系统Id systemId | w 站点Id siteId （包含租户id=0的数据）
     */
    public String SWedAlias() default "";

    /**
     * 租户表的别名 | 控制到 e enterpriseId | s 系统Id systemId | w 站点Id siteId | l 产品库Id libraryId （包含租户id=0的数据）
     */
    public String SWLedAlias() default "";

    /**
     * 租户更新控制的别名 | 控制到 e enterpriseId (empty 无前缀更新 | other 有前缀更新)
     */
    public String ueAlias() default "";

    /**
     * 租户更新控制的别名 | 控制到 e enterpriseId | s 系统Id systemId (empty 无前缀更新 | other 有前缀更新)
     */
    public String SueAlias() default "";

    /**
     * 租户更新控制的别名 | 控制到  e enterpriseId | w 站点Id siteId (empty 无前缀更新 | other 有前缀更新)
     */
    public String WueAlias() default "";

    /**
     * 租户更新控制的别名 | 控制到 e enterpriseId | s 系统Id systemId | w 站点Id siteId (empty 无前缀更新 | other 有前缀更新)
     */
    public String SWueAlias() default "";

    /**
     * 租户更新控制的别名 | 控制到 e enterpriseId | s 系统Id systemId | w 站点Id siteId | l 产品库Id libraryId (empty 无前缀更新 | other 有前缀更新)
     */
    public String SWLueAlias() default "";

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
