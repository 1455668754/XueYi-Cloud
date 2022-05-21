package com.xueyi.common.web.interceptor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.plugins.InterceptorIgnoreHelper;
import com.baomidou.mybatisplus.core.toolkit.ClassUtils;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.parser.JsqlParserSupport;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.toolkit.PropertyMapper;
import com.xueyi.common.web.handler.TenantLineHandler;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.util.*;

/**
 * 租户拦截器
 *
 * @author xueyi
 */
public class TenantLineInnerInterceptor extends JsqlParserSupport implements InnerInterceptor {

    /** 租户处理器 */
    private TenantLineHandler tenantLineHandler;

    public TenantLineInnerInterceptor() {
    }

    public TenantLineInnerInterceptor(final TenantLineHandler tenantLineHandler) {
        this.tenantLineHandler = tenantLineHandler;
    }

    public TenantLineHandler getTenantLineHandler() {
        return this.tenantLineHandler;
    }

    public void setTenantLineHandler(final TenantLineHandler tenantLineHandler) {
        this.tenantLineHandler = tenantLineHandler;
    }

    /**
     * before -> query
     */
    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        if (!InterceptorIgnoreHelper.willIgnoreTenantLine(ms.getId())) {
            PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql(boundSql);
            mpBs.sql(this.parserSingle(mpBs.sql(), null));
        }
    }

    /**
     * before -> prepare
     */
    @Override
    public void beforePrepare(StatementHandler sh, Connection connection, Integer transactionTimeout) {
        PluginUtils.MPStatementHandler mpSh = PluginUtils.mpStatementHandler(sh);
        MappedStatement ms = mpSh.mappedStatement();
        SqlCommandType sct = ms.getSqlCommandType();
        if (sct == SqlCommandType.INSERT || sct == SqlCommandType.UPDATE || sct == SqlCommandType.DELETE) {
            if (InterceptorIgnoreHelper.willIgnoreTenantLine(ms.getId())) {
                return;
            }
            PluginUtils.MPBoundSql mpBs = mpSh.mPBoundSql();
            mpBs.sql(this.parserMulti(mpBs.sql(), null));
        }
    }

    /**
     * select process
     */
    @Override
    protected void processSelect(Select select, int index, String sql, Object obj) {
        this.processSelectBody(select.getSelectBody());
        List<WithItem> withItemsList = select.getWithItemsList();
        if (CollUtil.isNotEmpty(withItemsList)) {
            withItemsList.forEach(this::processSelectBody);
        }
    }

    /**
     * select body process
     */
    protected void processSelectBody(SelectBody selectBody) {
        if (selectBody != null) {
            if (selectBody instanceof PlainSelect) {
                this.processPlainSelect((PlainSelect) selectBody);
            } else if (selectBody instanceof WithItem) {
                WithItem withItem = (WithItem) selectBody;
                this.processSelectBody(withItem.getSubSelect().getSelectBody());
            } else {
                SetOperationList operationList = (SetOperationList) selectBody;
                List<SelectBody> selectBodyList = operationList.getSelects();
                if (CollUtil.isNotEmpty(selectBodyList)) {
                    selectBodyList.forEach(this::processSelectBody);
                }
            }
        }
    }

    /**
     * 构造处理条件
     */
    protected Expression builderExpression(Expression currentExpression, List<Table> tables) {
        if (CollUtil.isEmpty(tables)) {
            return currentExpression;
        } else {
            Expression injectExpression = null;
            Expression tenantId = this.tenantLineHandler.getTenantId();
            Expression commonTenantId = this.tenantLineHandler.getCommonTenantId();

            // tenant control
            for (Table table : tables) {
                if (!this.tenantLineHandler.ignoreTable(table.getName())) {
                    if (this.tenantLineHandler.isCommonTable(table.getName())) {
                        InExpression inExpression = new InExpression();
                        inExpression.setLeftExpression(this.tenantLineHandler.getAliasColumn(table));
                        inExpression.setRightExpression(commonTenantId);
                        if (ObjectUtil.isNull(injectExpression))
                            injectExpression = inExpression;
                        else
                            injectExpression = new AndExpression(injectExpression, inExpression);
                    } else {
                        if (ObjectUtil.isNull(injectExpression))
                            injectExpression = new EqualsTo(this.tenantLineHandler.getAliasColumn(table), tenantId);
                        else
                            injectExpression = new AndExpression(injectExpression, new EqualsTo(this.tenantLineHandler.getAliasColumn(table), tenantId));
                    }
                }
            }

            if (ObjectUtil.isNull(injectExpression)) {
                return currentExpression;
            } else {
                return currentExpression == null
                        ? injectExpression
                        : currentExpression instanceof OrExpression
                        ? new AndExpression(new Parenthesis(currentExpression), injectExpression)
                        : new AndExpression(currentExpression, injectExpression);
            }
        }
    }

    /**
     * insert 语句处理
     */
    @Override
    protected void processInsert(Insert insert, int index, String sql, Object obj) {
        String tableName = insert.getTable().getName();
        if (!this.tenantLineHandler.ignoreTable(insert.getTable().getName())) {
            List<Column> columns = insert.getColumns();
            if (CollUtil.isNotEmpty(columns)) {
                List<Expression> duplicateUpdateColumns = insert.getDuplicateUpdateExpressionList();

                // tenant control
                String tenantIdColumn = this.tenantLineHandler.getTenantIdColumn();
                columns.add(new Column(tenantIdColumn));
                if (CollUtil.isNotEmpty(duplicateUpdateColumns)) {
                    duplicateUpdateColumns.add(new EqualsTo(new StringValue(tenantIdColumn), this.tenantLineHandler.getInsertTenantId(tableName)));
                }

                Select select = insert.getSelect();
                if (select != null) {
                    this.processInsertSelect(select.getSelectBody());
                } else {
                    if (insert.getItemsList() == null) {
                        throw ExceptionUtils.mpe("Failed to process multiple-table update, please exclude the tableName or statementId");
                    }

                    ItemsList itemsList = insert.getItemsList();
                    if (itemsList instanceof MultiExpressionList) {
                        ((MultiExpressionList) itemsList).getExpressionLists()
                                .forEach((el) -> el.getExpressions().add(this.tenantLineHandler.getInsertTenantId(tableName)));
                    } else {
                        ((ExpressionList) itemsList).getExpressions().add(this.tenantLineHandler.getInsertTenantId(tableName));
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
        if (!this.tenantLineHandler.ignoreTable(update.getTable().getName()))
            update.setWhere(this.tenantLineHandler.updateExpression(update.getTable(), update.getWhere()));
    }

    /**
     * delete 语句处理
     */
    @Override
    protected void processDelete(Delete delete, int index, String sql, Object obj) {
        if (!this.tenantLineHandler.ignoreTable(delete.getTable().getName()))
            delete.setWhere(this.tenantLineHandler.updateExpression(delete.getTable(), delete.getWhere()));
    }

    protected void processInsertSelect(SelectBody selectBody) {
        PlainSelect plainSelect = (PlainSelect) selectBody;
        FromItem fromItem = plainSelect.getFromItem();
        if (fromItem instanceof Table) {
            this.processPlainSelect(plainSelect);
            this.appendSelectItem(plainSelect.getSelectItems());
        } else if (fromItem instanceof SubSelect) {
            SubSelect subSelect = (SubSelect) fromItem;
            this.appendSelectItem(plainSelect.getSelectItems());
            this.processInsertSelect(subSelect.getSelectBody());
        }
    }

    protected void appendSelectItem(List<SelectItem> selectItems) {
        if (CollUtil.isNotEmpty(selectItems)) {
            if (selectItems.size() == 1) {
                SelectItem item = selectItems.get(0);
                if (item instanceof AllColumns || item instanceof AllTableColumns) {
                    return;
                }
            }
            // tenant control
            selectItems.add(new SelectExpressionItem(new Column(this.tenantLineHandler.getTenantIdColumn())));
        }
    }

    protected void processPlainSelect(PlainSelect plainSelect) {
        List<SelectItem> selectItems = plainSelect.getSelectItems();
        if (CollUtil.isNotEmpty(selectItems)) {
            selectItems.forEach(this::processSelectItem);
        }

        Expression where = plainSelect.getWhere();
        this.processWhereSubSelect(where);
        FromItem fromItem = plainSelect.getFromItem();
        List<Table> list = this.processFromItem(fromItem);
        List<Table> mainTables = new ArrayList<>(list);
        List<Join> joins = plainSelect.getJoins();
        if (CollUtil.isNotEmpty(joins)) {
            mainTables = this.processJoins(mainTables, joins);
        }

        if (CollUtil.isNotEmpty(mainTables)) {
            plainSelect.setWhere(this.builderExpression(where, mainTables));
        }
    }

    private List<Table> processFromItem(FromItem fromItem) {
        while (fromItem instanceof ParenthesisFromItem) {
            fromItem = ((ParenthesisFromItem) fromItem).getFromItem();
        }

        List<Table> mainTables = new ArrayList<>();
        if (fromItem instanceof Table) {
            Table fromTable = (Table) fromItem;
            mainTables.add(fromTable);
        } else if (fromItem instanceof SubJoin) {
            List<Table> tables = this.processSubJoin((SubJoin) fromItem);
            mainTables.addAll(tables);
        } else {
            this.processOtherFromItem(fromItem);
        }

        return mainTables;
    }

    protected void processWhereSubSelect(Expression where) {
        if (where != null) {
            if (where instanceof FromItem) {
                this.processOtherFromItem((FromItem) where);
            } else {
                if (where.toString().indexOf("SELECT") > 0) {
                    if (where instanceof BinaryExpression) {
                        BinaryExpression expression = (BinaryExpression) where;
                        this.processWhereSubSelect(expression.getLeftExpression());
                        this.processWhereSubSelect(expression.getRightExpression());
                    } else if (where instanceof InExpression) {
                        InExpression expression = (InExpression) where;
                        Expression inExpression = expression.getRightExpression();
                        if (inExpression instanceof SubSelect) {
                            this.processSelectBody(((SubSelect) inExpression).getSelectBody());
                        }
                    } else if (where instanceof ExistsExpression) {
                        ExistsExpression expression = (ExistsExpression) where;
                        this.processWhereSubSelect(expression.getRightExpression());
                    } else if (where instanceof NotExpression) {
                        NotExpression expression = (NotExpression) where;
                        this.processWhereSubSelect(expression.getExpression());
                    } else if (where instanceof Parenthesis) {
                        Parenthesis expression = (Parenthesis) where;
                        this.processWhereSubSelect(expression.getExpression());
                    }
                }

            }
        }
    }

    protected void processSelectItem(SelectItem selectItem) {
        if (selectItem instanceof SelectExpressionItem) {
            SelectExpressionItem selectExpressionItem = (SelectExpressionItem) selectItem;
            if (selectExpressionItem.getExpression() instanceof SubSelect) {
                this.processSelectBody(((SubSelect) selectExpressionItem.getExpression()).getSelectBody());
            } else if (selectExpressionItem.getExpression() instanceof Function) {
                this.processFunction((Function) selectExpressionItem.getExpression());
            }
        }

    }

    protected void processFunction(Function function) {
        ExpressionList parameters = function.getParameters();
        if (parameters != null) {
            parameters.getExpressions().forEach((expression) -> {
                if (expression instanceof SubSelect) {
                    this.processSelectBody(((SubSelect) expression).getSelectBody());
                } else if (expression instanceof Function) {
                    this.processFunction((Function) expression);
                }

            });
        }

    }

    protected void processOtherFromItem(FromItem fromItem) {
        while (fromItem instanceof ParenthesisFromItem) {
            fromItem = ((ParenthesisFromItem) fromItem).getFromItem();
        }

        if (fromItem instanceof SubSelect) {
            SubSelect subSelect = (SubSelect) fromItem;
            if (subSelect.getSelectBody() != null) {
                this.processSelectBody(subSelect.getSelectBody());
            }
        } else if (fromItem instanceof ValuesList) {
            this.logger.debug("Perform a subQuery, if you do not give us feedback");
        } else if (fromItem instanceof LateralSubSelect) {
            LateralSubSelect lateralSubSelect = (LateralSubSelect) fromItem;
            if (lateralSubSelect.getSubSelect() != null) {
                SubSelect subSelect = lateralSubSelect.getSubSelect();
                if (subSelect.getSelectBody() != null) {
                    this.processSelectBody(subSelect.getSelectBody());
                }
            }
        }

    }

    private List<Table> processSubJoin(SubJoin subJoin) {
        List<Table> mainTables = new ArrayList<>();
        if (subJoin.getJoinList() != null) {
            List<Table> list = this.processFromItem(subJoin.getLeft());
            mainTables.addAll(list);
            mainTables = this.processJoins(mainTables, subJoin.getJoinList());
        }
        return mainTables;
    }

    private List<Table> processJoins(List<Table> mainTables, List<Join> joins) {
        Table mainTable = null;
        Table leftTable = null;
        if (mainTables == null) {
            mainTables = new ArrayList<>();
        } else if (mainTables.size() == 1) {
            mainTable = mainTables.get(0);
            leftTable = mainTable;
        }

        Deque<List<Table>> onTableDeque = new LinkedList<>();

        for (Join join : joins) {
            FromItem joinItem = join.getRightItem();
            List<Table> joinTables = null;
            if (joinItem instanceof Table) {
                joinTables = new ArrayList<>();
                joinTables.add((Table) joinItem);
            } else if (joinItem instanceof SubJoin) {
                joinTables = this.processSubJoin((SubJoin) joinItem);
            }

            if (joinTables != null) {
                if (join.isSimple()) {
                    mainTables.addAll(joinTables);
                } else {
                    Table joinTable = joinTables.get(0);
                    List<Table> onTables = null;
                    if (join.isRight()) {
                        mainTable = joinTable;
                        if (leftTable != null) {
                            onTables = Collections.singletonList(leftTable);
                        }
                    } else if (join.isLeft()) {
                        onTables = Collections.singletonList(joinTable);
                    } else if (join.isInner()) {
                        onTables = mainTable == null
                                ? Collections.singletonList(joinTable)
                                : Arrays.asList(mainTable, joinTable);
                        mainTable = null;
                    }

                    mainTables = new ArrayList<>();
                    if (mainTable != null)
                        mainTables.add(mainTable);

                    Collection<Expression> originOnExpressions = join.getOnExpressions();
                    LinkedList<Expression> onExpressions;
                    if (originOnExpressions.size() == 1 && onTables != null) {
                        onExpressions = new LinkedList<>();
                        onExpressions.add(this.builderExpression(originOnExpressions.iterator().next(), onTables));
                        join.setOnExpressions(onExpressions);
                    } else {
                        onTableDeque.push(onTables);
                        if (originOnExpressions.size() > 1) {
                            onExpressions = new LinkedList<>();

                            for (Expression originOnExpression : originOnExpressions) {
                                List<Table> currentTableList = onTableDeque.poll();
                                if (CollUtil.isEmpty(currentTableList)) {
                                    onExpressions.add(originOnExpression);
                                } else {
                                    onExpressions.add(this.builderExpression(originOnExpression, currentTableList));
                                }
                            }
                            join.setOnExpressions(onExpressions);
                        }
                    }
                    leftTable = joinTable;
                }
            } else {
                this.processOtherFromItem(joinItem);
                leftTable = null;
            }
        }
        return mainTables;
    }

    @Override
    public void setProperties(Properties properties) {
        PropertyMapper.newInstance(properties).whenNotBlank("tenantLineHandler", ClassUtils::newInstance, this::setTenantLineHandler);
    }

    @Override
    public String toString() {
        return "BasicLineInnerInterceptor(super=" + super.toString() + ", tenantLineHandler=" + this.getTenantLineHandler() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof TenantLineInnerInterceptor)) {
            return false;
        } else {
            TenantLineInnerInterceptor other = (TenantLineInnerInterceptor) o;
            if (!other.canEqual(this)) {
                return false;
            } else if (!super.equals(o)) {
                return false;
            } else {
                Object this$basicLineHandler = this.getTenantLineHandler();
                Object other$basicLineHandler = other.getTenantLineHandler();
                if (this$basicLineHandler == null) {
                    return other$basicLineHandler == null;
                } else return this$basicLineHandler.equals(other$basicLineHandler);
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof TenantLineInnerInterceptor;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        Object $basicLineHandler = this.getTenantLineHandler();
        result = result * 59 + ($basicLineHandler == null ? 43 : $basicLineHandler.hashCode());
        return result;
    }
}
