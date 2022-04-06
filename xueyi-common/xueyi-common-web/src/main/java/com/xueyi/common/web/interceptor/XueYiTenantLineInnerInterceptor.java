package com.xueyi.common.web.interceptor;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.xueyi.common.web.handler.XueYiTenantLineHandler;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 租户拦截器
 *
 * @author xueyi
 */
public class XueYiTenantLineInnerInterceptor extends TenantLineInnerInterceptor {

    private XueYiTenantLineHandler tenantLineHandler;

    public XueYiTenantLineInnerInterceptor(final XueYiTenantLineHandler tenantLineHandler) {
        this.tenantLineHandler = tenantLineHandler;
        super.setTenantLineHandler(tenantLineHandler);
    }

    /**
     * 处理条件
     */
    @Override
    protected Expression builderExpression(Expression currentExpression, List<Table> tables) {
        if (CollectionUtils.isEmpty(tables)) {
            return currentExpression;
        } else {
            Expression tenantId = this.tenantLineHandler.getTenantId();
            Expression commonTenantId = this.tenantLineHandler.getCommonTenantId();
            List<EqualsTo> equalsTos = tables.stream()
                    .filter((x) -> !this.tenantLineHandler.ignoreTable(x.getName()) && !this.tenantLineHandler.isCommonTable(x.getName()))
                    .map((item) -> new EqualsTo(this.getAliasColumn(item), tenantId))
                    .collect(Collectors.toList());

            List<InExpression> inExpressions = tables.stream()
                    .filter((x) -> !this.tenantLineHandler.ignoreTable(x.getName()) && this.tenantLineHandler.isCommonTable(x.getName()))
                    .map((item) -> {
                        InExpression inExpression = new InExpression();
                        inExpression.setLeftExpression(this.getAliasColumn(item));
                        inExpression.setRightExpression(commonTenantId);
                        return inExpression;
                    }).collect(Collectors.toList());

            if (CollectionUtils.isEmpty(equalsTos) && CollectionUtils.isEmpty(inExpressions)) {
                return currentExpression;
            } else {
                Expression injectExpression = CollectionUtils.isNotEmpty(equalsTos) ? equalsTos.get(0) : inExpressions.get(0);
                for (int i = 1; i < equalsTos.size(); ++i) {
                    injectExpression = new AndExpression(injectExpression, equalsTos.get(i));
                }
                for (int i = equalsTos.size() > 0 ? 0 : 1; i < inExpressions.size(); ++i) {
                    injectExpression = new AndExpression(injectExpression, inExpressions.get(i));
                }
                if (currentExpression == null) {
                    return injectExpression;
                } else {
                    return currentExpression instanceof OrExpression ? new AndExpression(new Parenthesis(currentExpression), injectExpression) : new AndExpression(currentExpression, injectExpression);
                }
            }
        }
    }

    /**
     * insert 语句处理
     */
    @Override
    protected void processInsert(Insert insert, int index, String sql, Object obj) {
        if (!this.tenantLineHandler.ignoreTable(insert.getTable().getName())) {
            List<Column> columns = insert.getColumns();
            if (!CollectionUtils.isEmpty(columns)) {
                String tenantIdColumn = this.tenantLineHandler.getTenantIdColumn();
                if (!this.tenantLineHandler.ignoreInsert(columns, tenantIdColumn)) {

                    columns.add(new Column(tenantIdColumn));
                    List<Expression> duplicateUpdateColumns = insert.getDuplicateUpdateExpressionList();
                    if (CollectionUtils.isNotEmpty(duplicateUpdateColumns)) {
                        EqualsTo equalsTo = new EqualsTo();
                        equalsTo.setLeftExpression(new StringValue(tenantIdColumn));
                        equalsTo.setRightExpression(this.tenantLineHandler.getTenantId());
                        duplicateUpdateColumns.add(equalsTo);
                    }

                    Select select = insert.getSelect();
                    if (select != null) {
                        this.processInsertSelect(select.getSelectBody());
                    } else {
                        if (insert.getItemsList() == null) {
                            throw ExceptionUtils.mpe("Failed to process multiple-table update, please exclude the tableName or statementId", new Object[0]);
                        }

                        ItemsList itemsList = insert.getItemsList();
                        if (itemsList instanceof MultiExpressionList) {
                            ((MultiExpressionList) itemsList).getExpressionLists().forEach((el) -> {
                                el.getExpressions().add(this.tenantLineHandler.getTenantId());
                            });
                        } else {
                            ((ExpressionList) itemsList).getExpressions().add(this.tenantLineHandler.getTenantId());
                        }
                    }
                }
            }
        }
    }

    /**
     * update 语句处理
     */
    @Override
    protected void processUpdate(Update update, int index, String sql, Object obj) {
        final Table table = update.getTable();
        if (tenantLineHandler.ignoreTable(table.getName())) {
            // 过滤退出执行
            return;
        }
        // 核心逻辑：区分混合数据与租户数据
        if (this.tenantLineHandler.isCommonTable(table.getName()) && this.tenantLineHandler.isLessor())
            update.setWhere(this.inExpression(table, update.getWhere()));
        else
            update.setWhere(this.andExpression(table, update.getWhere()));
    }

    /**
     * delete 语句处理
     */
    @Override
    protected void processDelete(Delete delete, int index, String sql, Object obj) {
        final Table table = delete.getTable();
        if (tenantLineHandler.ignoreTable(delete.getTable().getName())) {
            // 过滤退出执行
            return;
        }
        // 核心逻辑：区分混合数据与租户数据
        if (this.tenantLineHandler.isCommonTable(table.getName()) && this.tenantLineHandler.isLessor())
            delete.setWhere(this.inExpression(table, delete.getWhere()));
        else
            delete.setWhere(this.andExpression(table, delete.getWhere()));
    }

    /**
     * delete update 语句 where 处理
     */
    protected Expression inExpression(Table table, Expression where) {
        //获得where条件表达式
        InExpression inExpression = new InExpression();
        inExpression.setLeftExpression(this.getAliasColumn(table));
        inExpression.setRightExpression(this.tenantLineHandler.getCommonTenantId());
        if (null != where)
            return where instanceof OrExpression
                    ? new AndExpression(inExpression, new Parenthesis(where))
                    : new AndExpression(inExpression, where);
        return inExpression;
    }
}
