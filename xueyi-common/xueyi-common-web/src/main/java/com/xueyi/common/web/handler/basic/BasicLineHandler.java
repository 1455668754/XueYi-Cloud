package com.xueyi.common.web.handler.basic;

import cn.hutool.core.util.ArrayUtil;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.basic.DictConstants;
import com.xueyi.common.core.constant.basic.TenantConstants;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.common.web.config.properties.TenantProperties;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.util.cnfexpression.MultipleExpression;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用租户处理器
 *
 * @author xueyi
 */
public interface BasicLineHandler {

    /**
     * 获取租户字段名
     *
     * @return 租户字段名
     */
    String getTenantIdColumn();

    /**
     * 租户表租户控制
     *
     * @return 租户值
     */
    Expression getTenantId();

    /**
     * 公共表租户控制 | insert
     *
     * @return 租户值
     */
    default Expression getMixTenantId() {
        CaseExpression caseExpression = new CaseExpression();
        WhenClause commonCase = new WhenClause();
        commonCase.setWhenExpression(new EqualsTo(new HexValue(TenantConstants.COMMON_ID), new StringValue(DictConstants.DicCommonPrivate.COMMON.getCode())));
        commonCase.setThenExpression(new LongValue(BaseConstants.COMMON_ID));
        caseExpression.setSwitchExpression(commonCase);
        caseExpression.setElseExpression(getTenantId());
        return caseExpression;
    }

    /**
     * 公共表租户控制 | select
     *
     * @return 租户值
     */
    default MultipleExpression getCommonTenantId() {
        List<Expression> childList = new ArrayList<>();
        if (SecurityUtils.isNotEmptyTenant())
            childList.add(new LongValue(BaseConstants.COMMON_ID));
        childList.add(getTenantId());
        return new MultipleExpression(childList) {
            @Override
            public String getStringExpression() {
                return ",";
            }
        };
    }

    /**
     * 忽略租户控制
     *
     * @return 结果
     */
    default boolean ignoreTable(String tableName) {
        return isTenantTable(tableName);
    }

    /**
     * 判断是否为公共表
     *
     * @return 结果
     */
    default boolean isCommonTable(String tableName) {
        return ArrayUtil.contains(TenantProperties.getCommonTable(), tableName);
    }

    /**
     * 判断是否为非租户表
     *
     * @return 结果
     */
    default boolean isExcludeTable(String tableName) {
        return ArrayUtil.contains(TenantProperties.getExcludeTable(), tableName);
    }

    /**
     * 判断是否为租户表
     *
     * @return 结果
     */
    default boolean isTenantTable(String tableName) {
        return !isExcludeTable(tableName);
    }

    /**
     * 判断是否为租管租户
     *
     * @return 结果
     */
    default boolean isLessor() {
        return SecurityUtils.isAdminTenant();
    }

    /**
     * 构造更新、删除租户表达式 | delete/update -> where
     *
     * @param table 表对象
     * @param where 表达式条件对象
     * @return Expression
     */
    default Expression updateExpression(Table table, Expression where) {
        return isCommonTable(table.getName()) && isLessor()
                ? inExpression(table, where)
                : this.andExpression(table, where);
    }

    /**
     * 租户表达式 | insert -> insert
     *
     * @param tableName 表名
     * @return Expression
     */
    default Expression getInsertTenantId(String tableName) {
        return isCommonTable(tableName)
                ? getMixTenantId()
                : getTenantId();
    }

    /**
     * 单租户表达式 | delete/update -> where
     *
     * @param table 表对象
     * @param where 表达式条件对象
     * @return Expression
     */
    default BinaryExpression andExpression(Table table, Expression where) {
        EqualsTo equalsTo = new EqualsTo();
        equalsTo.setLeftExpression(getAliasColumn(table));
        equalsTo.setRightExpression(getTenantId());
        if (null != where) {
            return where instanceof OrExpression ? new AndExpression(equalsTo, new Parenthesis(where)) : new AndExpression(equalsTo, where);
        } else {
            return equalsTo;
        }
    }

    /**
     * 混合租户表达式 | delete/update -> where common
     *
     * @param table 表对象
     * @param where 表达式条件对象
     * @return Expression
     */
    default Expression inExpression(Table table, Expression where) {
        InExpression inExpression = new InExpression();
        inExpression.setLeftExpression(getAliasColumn(table));
        inExpression.setRightExpression(getCommonTenantId());
        if (null != where)
            return where instanceof OrExpression
                    ? new AndExpression(inExpression, new Parenthesis(where))
                    : new AndExpression(inExpression, where);
        return inExpression;
    }

    /**
     * 获取租户别名列
     *
     * @param table 表对象
     * @return 租户别名列
     */
    default Column getAliasColumn(Table table) {
        StringBuilder column = new StringBuilder();
        if (table.getAlias() != null)
            column.append(table.getAlias().getName());
        else
            column.append(table.getName());
        column.append(".").append(getTenantIdColumn());
        return new Column(column.toString());
    }
}
