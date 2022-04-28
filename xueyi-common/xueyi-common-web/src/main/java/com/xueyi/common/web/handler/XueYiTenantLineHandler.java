package com.xueyi.common.web.handler;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.xueyi.common.core.constant.basic.DictConstants;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.TenantConstants;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.common.web.annotation.TenantIgnore;
import com.xueyi.common.web.config.properties.TenantProperties;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.util.cnfexpression.MultipleExpression;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 租户处理器
 *
 * @author xueyi
 */
@Aspect
@Component
public class XueYiTenantLineHandler implements TenantLineHandler {

    @Autowired
    private TenantProperties tenantProperties;

    /**
     * 通过ThreadLocal记录租户控制相关的属性值
     */
    public ThreadLocal<TenantIgnore> threadLocal = new ThreadLocal<>();

    /**
     * 清空当前线程上次保存的租户控制信息
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

    /**
     * 租户表租户控制
     *
     * @return 租户值
     */
    @Override
    public Expression getTenantId() {
        return new LongValue(SecurityUtils.getEnterpriseId());
    }

    /**
     * 公共表租户控制 | insert
     *
     * @return 租户值
     */
    public Expression getMixTenantId(){
        CaseExpression caseExpression = new CaseExpression();
        WhenClause commonCase = new WhenClause();
        commonCase.setWhenExpression(new EqualsTo(new HexValue(TenantConstants.COMMON_ID), new StringValue(DictConstants.DicCommonPrivate.COMMON.getCode())));
        commonCase.setThenExpression(new LongValue(SecurityConstants.COMMON_TENANT_ID));
        caseExpression.setSwitchExpression(commonCase);
        caseExpression.setElseExpression(this.getTenantId());
        return caseExpression;
    }

    /**
     * 公共表租户控制 | select
     *
     * @return 租户值
     */
    public MultipleExpression getCommonTenantId() {
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

    /**
     * 获取租户字段名
     *
     * @return 租户字段名
     */
    @Override
    public String getTenantIdColumn() {
        return TenantConstants.TENANT_ID;
    }

    /**
     * 忽略租户控制
     *
     * @return 结果
     */
    @Override
    public boolean ignoreTable(String tableName) {
        if (isExcludeTable(tableName)) {
            return true;
        } else {
            TenantIgnore tenantIgnore = threadLocal.get();
            return ObjectUtil.isNotNull(tenantIgnore) && tenantIgnore.tenantLine();
        }
    }

    /**
     * 忽略租户控制
     *
     * @return 结果
     */
    @Override
    public boolean ignoreInsert(List<Column> columns, String tenantIdColumn) {
        return TenantLineHandler.super.ignoreInsert(columns, tenantIdColumn);
    }

    /**
     * 判断是否为公共表
     *
     * @return 结果
     */
    public boolean isCommonTable(String tableName) {
        return ArrayUtil.contains(tenantProperties.getCommonTable(), tableName);
    }

    /**
     * 判断是否为非租户表
     *
     * @return 结果
     */
    public boolean isExcludeTable(String tableName) {
        return ArrayUtil.contains(tenantProperties.getExcludeTable(), tableName);
    }

    /**
     * 判断是否为租管租户
     *
     * @return 结果
     */
    public boolean isLessor() {
        return SecurityUtils.isAdminTenant();
    }
}
