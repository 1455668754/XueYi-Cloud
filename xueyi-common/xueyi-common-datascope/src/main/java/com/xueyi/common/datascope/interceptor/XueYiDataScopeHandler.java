package com.xueyi.common.datascope.interceptor;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import com.xueyi.common.core.constant.system.AuthorityConstants;
import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.system.api.model.LoginUser;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.HexValue;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.schema.Column;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 数据权限处理器
 *
 * @author xueyi
 */
@Aspect
@Component
public class XueYiDataScopeHandler implements DataPermissionHandler {

    /**
     * 通过ThreadLocal记录权限相关的属性值
     */
    public ThreadLocal<DataScope> threadLocal = new ThreadLocal<>();

    /**
     * 清空当前线程上次保存的权限信息
     */
    @After("@annotation(controllerDataScope)")
    public void clearThreadLocal(DataScope controllerDataScope) {
        threadLocal.remove();
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    @Before("@annotation(controllerDataScope)")
    public void doBefore(DataScope controllerDataScope) {
        // 获得注解
        if (controllerDataScope != null)
            threadLocal.set(controllerDataScope);
    }

    /**
     * @param where             原SQL Where 条件表达式
     * @param mappedStatementId Mapper接口方法ID
     * @return 结果
     */
    @Override
    public Expression getSqlSegment(Expression where, String mappedStatementId) {
        DataScope dataScope = threadLocal.get();
        if (ObjectUtil.isNull(dataScope) || (StrUtil.isAllEmpty(dataScope.deptAlias(), dataScope.postAlias(), dataScope.userAlias())))
            return where;
        List<String> split = StrUtil.split(mappedStatementId, '.');
        int index = split.size();
        String method = split.get(index - 1);
        String mapper = split.get(index - 2);
        if (!((ArrayUtil.isEmpty(dataScope.mapperScope()) || ArrayUtil.contains(dataScope.mapperScope(), mapper))
                && (ArrayUtil.isEmpty(dataScope.methodScope()) || ArrayUtil.contains(dataScope.methodScope(), method))))
            return where;
        if (where == null)
            where = new HexValue(" 1 = 1 ");
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (ObjectUtil.isNull(loginUser) || loginUser.getUser().isAdmin())
            return where;
        Long userId = loginUser.getUserId();
        String scope = loginUser.getScope().getDataScope();
        switch (Objects.requireNonNull(AuthorityConstants.DataScope.getValue(scope))) {
            case ALL:
                return where;
            case CUSTOM:
            case DEPT:
            case DEPT_AND_CHILD:
            case POST:
                if (StrUtil.isNotEmpty(dataScope.userAlias())) {
                    Set<Long> userScope = loginUser.getScope().getUserScope();
                    ItemsList itemsList = new ExpressionList(userScope.stream().map(LongValue::new).collect(Collectors.toList()));
                    InExpression userInExpression = new InExpression(new Column(dataScope.userAlias()), itemsList);
                    return new AndExpression(where, userInExpression);
                } else if (StrUtil.isNotEmpty(dataScope.postAlias())) {
                    Set<Long> postScope = loginUser.getScope().getPostScope();
                    ItemsList itemsList = new ExpressionList(postScope.stream().map(LongValue::new).collect(Collectors.toList()));
                    InExpression postInExpression = new InExpression(new Column(dataScope.postAlias()), itemsList);
                    return new AndExpression(where, postInExpression);
                } else if (StrUtil.isNotEmpty(dataScope.deptAlias())) {
                    Set<Long> deptScope = loginUser.getScope().getDeptScope();
                    ItemsList itemsList = new ExpressionList(deptScope.stream().map(LongValue::new).collect(Collectors.toList()));
                    InExpression deptInExpression = new InExpression(new Column(dataScope.deptAlias()), itemsList);
                    return new AndExpression(where, deptInExpression);
                }
                break;
            case SELF:
                if (StrUtil.isNotEmpty(dataScope.userAlias())) {
                    EqualsTo selfEqualsTo = new EqualsTo();
                    selfEqualsTo.setLeftExpression(new Column(dataScope.userAlias()));
                    selfEqualsTo.setRightExpression(new LongValue(userId));
                    return new AndExpression(where, selfEqualsTo);
                }
                break;
            case NONE:
            default:
                break;
        }
        EqualsTo noEqualsTo = new EqualsTo();
        noEqualsTo.setLeftExpression(new Column("1"));
        noEqualsTo.setRightExpression(new LongValue("0"));
        return new AndExpression(where, noEqualsTo);
    }
}
