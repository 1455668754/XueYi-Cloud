package com.xueyi.common.web.handler;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.TenantConstants;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.common.web.annotation.TenantIgnore;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.util.cnfexpression.MultipleExpression;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 租户处理器
 *
 * @author xueyi
 */
@Aspect
@Component
public class XueYiTenantLineHandler implements TenantLineHandler {

    /**
     * 通过ThreadLocal记录权限相关的属性值
     */
    public ThreadLocal<TenantIgnore> threadLocal = new ThreadLocal<>();

    /**
     * 清空当前线程上次保存的权限信息
     */
    @After("@annotation(controllerTenantIgnore)")
    public void clearThreadLocal(TenantIgnore controllerTenantIgnore) {
        threadLocal.remove();
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    @Before("@annotation(controllerTenantIgnore)")
    public void doBefore(TenantIgnore controllerTenantIgnore) {
        // 获得注解
        if (controllerTenantIgnore != null)
            threadLocal.set(controllerTenantIgnore);
    }

    @Override
    public Expression getTenantId() {
        return new LongValue(SecurityUtils.getEnterpriseId());
    }

    public Expression getCommonTenantId() {
        List<Expression> childList = new ArrayList<>();
        if (SecurityUtils.isNotEmptyTenant())
            childList.add(new LongValue(SecurityConstants.COMMON_TENANT_ID));
        childList.add(new LongValue(SecurityUtils.getEnterpriseId()));
        return new MultipleExpression(childList) {
            @Override
            public String getStringExpression() {
                return ",";
            }
        };
    }

    @Override
    public String getTenantIdColumn() {
        return TenantConstants.TENANT_ID;
    }

    @Override
    public boolean ignoreTable(String tableName) {
        TenantIgnore tenantIgnore = threadLocal.get();
        return (ObjectUtil.isNotNull(tenantIgnore) && tenantIgnore.tenantLine()) || Arrays.asList(TenantConstants.EXCLUDE_TENANT_TABLE).contains(tableName);
    }

    @Override
    public boolean ignoreInsert(List<Column> columns, String tenantIdColumn) {
        return TenantLineHandler.super.ignoreInsert(columns, tenantIdColumn);
    }

    public boolean isCommonTable(String tableName) {
        return Arrays.asList(TenantConstants.COMMON_TENANT_TABLE).contains(tableName);
    }

    public boolean isLessor() {
        return SecurityUtils.isAdminTenant();
    }
}
