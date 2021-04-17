package com.xueyi.common.datascope.aspect;

import java.lang.reflect.Method;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.xueyi.system.api.organize.SysEnterprise;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.web.domain.BaseEntity;
import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.common.security.service.TokenService;
import com.xueyi.system.api.authority.SysRole;
import com.xueyi.system.api.organize.SysUser;
import com.xueyi.system.api.model.LoginUser;

/**
 * 数据过滤处理
 *
 * @author xueyi
 * @originalAuthor ruoyi
 */
@Aspect
@Component
public class DataScopeAspect
{
    /**
     * 全部数据权限
     */
    public static final String DATA_SCOPE_ALL = "1";

    /**
     * 自定数据权限
     */
    public static final String DATA_SCOPE_CUSTOM = "2";

    /**
     * 部门数据权限
     */
    public static final String DATA_SCOPE_DEPT = "3";

    /**
     * 部门及以下数据权限
     */
    public static final String DATA_SCOPE_DEPT_AND_CHILD = "4";

    /**
     * 仅本人数据权限
     */
    public static final String DATA_SCOPE_SELF = "5";

    /**
     * 数据权限过滤关键字
     */
    public static final String DATA_SCOPE = "dataScope";

    /**
     * 数据权限过滤关键字|更新
     */
    public static final String UPDATE_SCOPE = "updateScope";

    @Autowired
    private TokenService tokenService;

    // 配置织入点
    @Pointcut("@annotation(com.xueyi.common.datascope.annotation.DataScope)")
    public void dataScopePointCut()
    {
    }

    @Before("dataScopePointCut()")
    public void doBefore(JoinPoint point) throws Throwable
    {
        handleDataScope(point);
    }

    protected void handleDataScope(final JoinPoint joinPoint)
    {
        // 获得注解
        DataScope controllerDataScope = getAnnotationLog(joinPoint);
        if (controllerDataScope == null)
        {
            return;
        }
        // 获取当前的用户
        LoginUser loginUser = tokenService.getLoginUser();

        if (StringUtils.isNotNull(loginUser)) {
            SysUser currentUser = loginUser.getSysUser();
            SysEnterprise currentEnterprise = loginUser.getSysEnterprise();

            if (StringUtils.isNotNull(currentUser)) {
                dataScopeFilter(joinPoint, currentEnterprise, currentUser, controllerDataScope.loginEnterpriseAlias(), controllerDataScope.enterpriseAlias(), controllerDataScope.systemAlias(), controllerDataScope.updateEnterpriseAlias(), controllerDataScope.deptAlias(),
                        controllerDataScope.userAlias());
            }
        }
    }

    /**
     * 数据范围过滤
     *
     * @param joinPoint             切点
     * @param enterprise            租户
     * @param user                  用户
     * @param loginEnterpriseAlias  登录验证
     * @param enterpriseAlias       租户别名
     * @param systemAlias           租户+系统基础别名
     * @param updateEnterpriseAlias 租户更新控制的别名 empty 无前缀更新|other 有前缀更新
     * @param deptAlias             部门别名
     * @param userAlias             用户别名
     */
    public static void dataScopeFilter(JoinPoint joinPoint, SysEnterprise enterprise, SysUser user, String loginEnterpriseAlias, String enterpriseAlias, String systemAlias, String updateEnterpriseAlias, String deptAlias, String userAlias) {
        StringBuilder sqlString = new StringBuilder();
        StringBuilder upSqlString = new StringBuilder();

        // 如果是超级管理员，则不过滤数据
        if (!user.isAdmin()) {
            for (SysRole role : user.getRoles()) {
                String dataScope = role.getDataScope();
                if (DATA_SCOPE_ALL.equals(dataScope)) {
                    sqlString = new StringBuilder();
                    break;
                } else if (DATA_SCOPE_CUSTOM.equals(dataScope) && StringUtils.isNotBlank(deptAlias)) {
                    sqlString.append(StringUtils.format(
                            " OR {}.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = {} ) ", deptAlias,
                            role.getRoleId()));
                } else if (DATA_SCOPE_DEPT.equals(dataScope) && StringUtils.isNotBlank(deptAlias)) {
                    sqlString.append(StringUtils.format(" OR {}.dept_id = {} ", deptAlias, user.getDeptId()));
                } else if (DATA_SCOPE_DEPT_AND_CHILD.equals(dataScope) && StringUtils.isNotBlank(deptAlias)) {
                    sqlString.append(StringUtils.format(
                            " OR {}.dept_id IN ( SELECT dept_id FROM sys_dept WHERE dept_id = {} or find_in_set( {} , ancestors ) )",
                            deptAlias, user.getDeptId(), user.getDeptId()));
                } else if (DATA_SCOPE_SELF.equals(dataScope)) {
                    if (StringUtils.isNotBlank(userAlias)) {
                        sqlString.append(StringUtils.format(" OR {}.user_id = {} ", userAlias, user.getUserId()));
                    } else {
                        // 数据权限为仅本人且没有userAlias别名不查询任何数据
                        sqlString.append(" AND 1=0 ");
                    }
                }
            }
        }
        //1.租户数据查询分离模式1(仅当具备租户别名时生效)
        if (StringUtils.isNotBlank(enterpriseAlias)) {
            sqlString.append(StringUtils.format(" AND {}.tenant_id = {} AND {}.del_flag = 0", enterpriseAlias, enterprise.getEnterpriseId(), enterpriseAlias));
        }
        //2.租户数据查询分离模式2(仅当具备系统别名时生效(除生效模式1意外，还额外查询系统默认数据)
        else if (StringUtils.isNotBlank(systemAlias)) {
            sqlString.append(StringUtils.format(" AND {}.tenant_id IN (0, {}) AND {}.del_flag = 0", systemAlias, enterprise.getEnterpriseId(), systemAlias));
        }
        //3.租户数据更新分离模式(仅当具备更新控制别名时生效)(empty模式为无别名模式，other为有别名模式)
        else if (StringUtils.isNotBlank(updateEnterpriseAlias)) {
            if (updateEnterpriseAlias.equals("empty")) {
                upSqlString.append(StringUtils.format(" AND tenant_id = {} AND del_flag = 0", enterprise.getEnterpriseId()));
            } else {
                upSqlString.append(StringUtils.format(" AND {}.tenant_id = {} AND {}.del_flag = 0", updateEnterpriseAlias, enterprise.getEnterpriseId(), updateEnterpriseAlias));
            }
        } else if (StringUtils.isNotBlank(loginEnterpriseAlias)) {
            sqlString.append(StringUtils.format(" AND {}.del_flag = 0", loginEnterpriseAlias));
        }
        //4.当无查询控制时，阻断查询进行，保证数据安全(后续可跟进设置总领超管系统，解除权限设置)
        else {
            sqlString.append("1 = 0");
            upSqlString.append("1 = 0");
        }

        if (StringUtils.isNotBlank(sqlString.toString()) || StringUtils.isNotBlank(upSqlString.toString())) {
            Object params = joinPoint.getArgs()[0];
            if (StringUtils.isNotNull(params) && params instanceof BaseEntity) {
                BaseEntity baseEntity = (BaseEntity) params;
                baseEntity.setEnterpriseId(enterprise.getEnterpriseId());
                baseEntity.setCreateBy(user.getUserId());
                baseEntity.setUpdateBy(user.getUserId());
                if (StringUtils.isNotBlank(sqlString.toString())) {
                    baseEntity.getParams().put(DATA_SCOPE, " AND (" + sqlString.substring(4) + ")");
                }
                if (StringUtils.isNotBlank(upSqlString.toString())) {

                    //终端ID
                    int workerId = RandomUtil.randomInt(0, 31);
                    //数据中心ID
                    int datacenterId = 1;
                    Snowflake snowflake = IdUtil.getSnowflake(workerId, datacenterId);
                    Long id = snowflake.nextId();
                    baseEntity.setId(id);
                    baseEntity.getParams().put(UPDATE_SCOPE, " AND (" + upSqlString.substring(4) + ")");
                }
            }
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private DataScope getAnnotationLog(JoinPoint joinPoint)
    {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null)
        {
            return method.getAnnotation(DataScope.class);
        }
        return null;
    }
}
