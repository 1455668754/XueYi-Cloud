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
 */
@Aspect
@Component
public class DataScopeAspect {
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
     * 岗位数据权限
     */
    public static final String DATA_SCOPE_POST = "5";

    /**
     * 仅本人数据权限
     */
    public static final String DATA_SCOPE_SELF = "6";

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
    public void dataScopePointCut() {
    }

    @Before("dataScopePointCut()")
    public void doBefore(JoinPoint point) throws Throwable {
        handleDataScope(point);
    }

    protected void handleDataScope(final JoinPoint joinPoint) {
        // 获得注解
        DataScope controllerDataScope = getAnnotationLog(joinPoint);
        if (controllerDataScope == null) {
            return;
        }
        // 获取当前的用户
        LoginUser loginUser = tokenService.getLoginUser();

        if (StringUtils.isNotNull(loginUser)) {
            SysUser currentUser = loginUser.getSysUser();
            SysEnterprise currentEnterprise = loginUser.getSysEnterprise();

            if (StringUtils.isNotNull(currentUser)) {
                dataScopeFilter(joinPoint, currentEnterprise, currentUser,
                        controllerDataScope.eAlias(), controllerDataScope.SeAlias(), controllerDataScope.WeAlias(), controllerDataScope.SWeAlias(), controllerDataScope.SWLeAlias(),
                        controllerDataScope.edAlias(), controllerDataScope.SedAlias(), controllerDataScope.WedAlias(), controllerDataScope.SWedAlias(), controllerDataScope.SWLedAlias(),
                        controllerDataScope.ueAlias(), controllerDataScope.SueAlias(), controllerDataScope.WueAlias(), controllerDataScope.SWueAlias(), controllerDataScope.SWLueAlias(),
                        controllerDataScope.deptAlias(), controllerDataScope.postAlias(), controllerDataScope.userAlias());
            }
        }
    }

    /**
     * 数据范围过滤
     *
     * @param joinPoint  切点
     * @param enterprise 租户
     * @param user       用户
     * @param eAlias     租户表的别名 | 控制到 e enterpriseId
     * @param SeAlias    租户表的别名 | 控制到 e enterpriseId | s 系统Id systemId
     * @param WeAlias    租户表的别名 | 控制到 e enterpriseId | w 站点Id siteId
     * @param SWeAlias   租户表的别名 | 控制到 e enterpriseId | s 系统Id systemId | w 站点Id siteId
     * @param SWLeAlias  租户表的别名 | 控制到 e enterpriseId | s 系统Id systemId | w 站点Id siteId | l 产品库Id libraryId
     * @param edAlias    租户表的别名 | 控制到 e enterpriseId （包含租户id=0的数据）
     * @param SedAlias   租户表的别名 | 控制到 e enterpriseId | s 系统Id systemId （包含租户id=0的数据）
     * @param WedAlias   租户表的别名 | 控制到 e enterpriseId | w 站点Id siteId （包含租户id=0的数据）
     * @param SWedAlias  租户表的别名 | 控制到 e enterpriseId | s 系统Id systemId | w 站点Id siteId （包含租户id=0的数据）
     * @param SWLedAlias 租户表的别名 | 控制到 e enterpriseId | s 系统Id systemId | w 站点Id siteId | l 产品库Id libraryId （包含租户id=0的数据）
     * @param ueAlias    租户更新控制的别名 | 控制到 e enterpriseId (empty 无前缀更新 | other 有前缀更新)
     * @param SueAlias   租户更新控制的别名 | 控制到 e enterpriseId | s 系统Id systemId (empty 无前缀更新 | other 有前缀更新)
     * @param WueAlias   租户更新控制的别名 | 控制到 e enterpriseId | w 站点Id siteId (empty 无前缀更新 | other 有前缀更新)
     * @param SWueAlias  租户更新控制的别名 | 控制到 e enterpriseId | s 系统Id systemId | w 站点Id siteId (empty 无前缀更新 | other 有前缀更新)
     * @param SWLueAlias 租户更新控制的别名 | 控制到 e enterpriseId | s 系统Id systemId | w 站点Id siteId | l 产品库Id libraryId (empty 无前缀更新 | other 有前缀更新)
     * @param deptAlias  部门别名
     * @param postAlias  岗位别名
     * @param userAlias  用户别名
     */
    public static void dataScopeFilter(JoinPoint joinPoint, SysEnterprise enterprise, SysUser user, String eAlias, String SeAlias, String WeAlias, String SWeAlias, String SWLeAlias, String edAlias, String SedAlias, String WedAlias, String SWedAlias, String SWLedAlias, String ueAlias, String SueAlias, String WueAlias, String SWueAlias, String SWLueAlias, String deptAlias, String postAlias, String userAlias) {
        //默认只获取第一个参数
        Object params = joinPoint.getArgs()[0];
        StringBuilder sqlString = new StringBuilder();
        StringBuilder upSqlString = new StringBuilder();

        // 如果是超级管理员，则不过滤数据
        if (!user.isAdmin()) {
            for (SysRole role : user.getRoles()) {
                String dataScope = role.getDataScope();
                // 1.全部数据权限
                if (DATA_SCOPE_ALL.equals(dataScope)) {
                    sqlString = new StringBuilder();
                    break;
                }
                // 2.自定数据权限
                else if (DATA_SCOPE_CUSTOM.equals(dataScope) && StringUtils.isNotBlank(deptAlias) && StringUtils.isNotBlank(postAlias)) {
                    sqlString.append(StringUtils.format(
                            " OR {}.dept_id IN ( SELECT dept_post_id FROM sys_role_dept_post WHERE role_id = {} ) OR {}.post_id IN ( SELECT dept_post_id FROM sys_role_dept_post WHERE role_id = {} )", deptAlias,
                            role.getRoleId(), postAlias, role.getRoleId()));
                }
                // 3.本部门数据权限
                else if (DATA_SCOPE_DEPT.equals(dataScope) && StringUtils.isNotBlank(deptAlias)) {
                    sqlString.append(StringUtils.format(" OR {}.dept_id = {} ", deptAlias, user.getDeptId()));
                }
                // 4.本部门及以下数据权限
                else if (DATA_SCOPE_DEPT_AND_CHILD.equals(dataScope) && StringUtils.isNotBlank(deptAlias)) {
                    sqlString.append(StringUtils.format(
                            " OR {}.dept_id IN ( SELECT dept_id FROM sys_dept WHERE dept_id = {} or find_in_set( {} , ancestors ) )",
                            deptAlias, user.getDeptId(), user.getDeptId()));
                }
                // 5.本岗位数据权限
                else if (DATA_SCOPE_POST.equals(dataScope) && StringUtils.isNotBlank(postAlias)) {
                    sqlString.append(StringUtils.format(" OR {}.post_id = {} ", postAlias, user.getPostId()));
                }
                // 6.仅本人数据权限
                else if (DATA_SCOPE_SELF.equals(dataScope)) {
                    if (StringUtils.isNotBlank(userAlias)) {
                        sqlString.append(StringUtils.format(" OR {}.user_id = {} ", userAlias, user.getUserId()));
                    } else {
                        // 数据权限为仅本人且没有userAlias别名不查询任何数据
                        sqlString.append(" AND 1 = 0 ");
                    }
                }
            }
        }

        // 租户控制

        //控制数据权限 分离
        if (StringUtils.isNotBlank(deptAlias)) {
            sqlString.append(StringUtils.format(" AND {}.tenant_id = {}", deptAlias, enterprise.getEnterpriseId(), deptAlias));
        }
        if (StringUtils.isNotBlank(postAlias)) {
            sqlString.append(StringUtils.format(" AND {}.tenant_id = {}", postAlias, enterprise.getEnterpriseId(), postAlias));
        }
        if (StringUtils.isNotBlank(userAlias)) {
            sqlString.append(StringUtils.format(" AND {}.tenant_id = {}", userAlias, enterprise.getEnterpriseId(), userAlias));
        }

        //一般数据查询|更新分离模式
        if (StringUtils.isNotBlank(eAlias) || StringUtils.isNotBlank(edAlias) || StringUtils.isNotBlank(ueAlias)) {
            //数据查询分离模式1
            if (StringUtils.isNotBlank(eAlias)) {
                sqlString.append(StringUtils.format(" AND {}.tenant_id = {} AND {}.del_flag = 0", eAlias, enterprise.getEnterpriseId(), eAlias));
            }
            //数据查询分离模式2
            else if (StringUtils.isNotBlank(edAlias)) {
                sqlString.append(StringUtils.format(" AND ( {}.tenant_id = {} or {}.tenant_id = 0 ) AND {}.del_flag = 0", edAlias, enterprise.getEnterpriseId(), edAlias, edAlias));
            }
            //数据更新分离模式
            else if (StringUtils.isNotBlank(ueAlias)) {
                if (ueAlias.equals("empty")) {
                    upSqlString.append(StringUtils.format(" AND tenant_id = {} AND del_flag = 0", enterprise.getEnterpriseId()));
                } else {
                    upSqlString.append(StringUtils.format(" AND {}.tenant_id = {} AND {}.del_flag = 0", ueAlias, enterprise.getEnterpriseId(), ueAlias));
                }
            }
        }
        //复杂数据查询|更新分离模式
        else if (StringUtils.isNotNull(params) && params instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) params;
            //数据查询分离模式1
            if (StringUtils.isNotBlank(SeAlias) || StringUtils.isNotBlank(WeAlias) || StringUtils.isNotBlank(SWeAlias) || StringUtils.isNotBlank(SWLeAlias)) {
                if (StringUtils.isNotBlank(SeAlias)) {
                    sqlString.append(StringUtils.format(" AND {}.system_id = {} AND {}.tenant_id = {} AND {}.del_flag = 0", SeAlias, baseEntity.getSystemId(), SeAlias, enterprise.getEnterpriseId(), SeAlias));
                } else if (StringUtils.isNotBlank(WeAlias)) {
                    sqlString.append(StringUtils.format(" AND {}.site_id = {} AND {}.tenant_id = {} AND {}.del_flag = 0", WeAlias, baseEntity.getSiteId(), WeAlias, enterprise.getEnterpriseId(), WeAlias));
                } else if (StringUtils.isNotBlank(SWeAlias)) {
                    sqlString.append(StringUtils.format(" AND {}.system_id = {} AND {}.site_id = {} AND {}.tenant_id = {} AND {}.del_flag = 0", SWeAlias, baseEntity.getSystemId(), SWeAlias, baseEntity.getSiteId(), SWeAlias, enterprise.getEnterpriseId(), SWeAlias));
                } else if (StringUtils.isNotBlank(SWLeAlias)) {
                    sqlString.append(StringUtils.format(" AND {}.system_id = {} AND {}.site_id = {} AND {}.library_id = {} AND {}.tenant_id = {} AND {}.del_flag = 0", SWLeAlias, baseEntity.getSystemId(), SWLeAlias, baseEntity.getSiteId(), SWLeAlias, baseEntity.getLibraryId(), SWLeAlias, enterprise.getEnterpriseId(), SWLeAlias));
                }
            }
            //数据查询分离模式2
            else if (StringUtils.isNotBlank(SedAlias) || StringUtils.isNotBlank(WedAlias) || StringUtils.isNotBlank(SWedAlias) || StringUtils.isNotBlank(SWLedAlias)) {
                if (StringUtils.isNotBlank(SedAlias)) {
                    sqlString.append(StringUtils.format(" AND {}.system_id = {} AND ( {}.tenant_id = {} or {}.tenant_id = 0 ) AND {}.del_flag = 0", SedAlias, baseEntity.getSystemId(), SedAlias, enterprise.getEnterpriseId(), SedAlias, SedAlias));
                } else if (StringUtils.isNotBlank(WedAlias)) {
                    sqlString.append(StringUtils.format(" AND {}.site_id = {} AND ( {}.tenant_id = {} or {}.tenant_id = 0 ) AND {}.del_flag = 0", WedAlias, baseEntity.getSiteId(), WedAlias, enterprise.getEnterpriseId(), WedAlias));
                } else if (StringUtils.isNotBlank(SWedAlias)) {
                    sqlString.append(StringUtils.format(" AND {}.system_id = {} AND {}.site_id = {} AND ( {}.tenant_id = {} or {}.tenant_id = 0 ) AND {}.del_flag = 0", SWedAlias, baseEntity.getSystemId(), SWedAlias, baseEntity.getSiteId(), SWedAlias, enterprise.getEnterpriseId(), SWedAlias));
                } else if (StringUtils.isNotBlank(SWLedAlias)) {
                    sqlString.append(StringUtils.format(" AND {}.system_id = {} AND {}.site_id = {} AND {}.library_id = {} AND ( {}.tenant_id = {} or {}.tenant_id = 0 ) AND {}.del_flag = 0", SWLedAlias, baseEntity.getSystemId(), SWLedAlias, baseEntity.getSiteId(), SWLedAlias, baseEntity.getLibraryId(), SWLedAlias, enterprise.getEnterpriseId(), SWLedAlias));
                }
            }
            //数据更新分离模式
            else if (StringUtils.isNotBlank(SueAlias) || StringUtils.isNotBlank(WueAlias) || StringUtils.isNotBlank(SWueAlias) || StringUtils.isNotBlank(SWLueAlias)) {
                if (StringUtils.isNotBlank(SueAlias)) {
                    if (SueAlias.equals("empty")) {
                        upSqlString.append(StringUtils.format(" AND system_id = {} AND tenant_id = {} AND del_flag = 0", baseEntity.getSystemId(), enterprise.getEnterpriseId()));
                    } else {
                        upSqlString.append(StringUtils.format(" AND {}.system_id = {} AND {}.tenant_id = {} AND {}.del_flag = 0", SueAlias, baseEntity.getSystemId(), SueAlias, enterprise.getEnterpriseId(), SueAlias));
                    }
                } else if (StringUtils.isNotBlank(WueAlias)) {
                    if (WueAlias.equals("empty")) {
                        upSqlString.append(StringUtils.format(" AND site_id = {} AND tenant_id = {} AND del_flag = 0", baseEntity.getSiteId(), enterprise.getEnterpriseId()));
                    } else {
                        upSqlString.append(StringUtils.format(" AND {}.site_id = {} AND {}.tenant_id = {} AND {}.del_flag = 0", WueAlias, baseEntity.getSiteId(), WueAlias, enterprise.getEnterpriseId(), WueAlias));
                    }
                } else if (StringUtils.isNotBlank(SWueAlias)) {
                    if (SWueAlias.equals("empty")) {
                        upSqlString.append(StringUtils.format(" AND system_id = {} AND site_id = {} AND tenant_id = {} AND del_flag = 0", baseEntity.getSystemId(), baseEntity.getSiteId(), enterprise.getEnterpriseId()));
                    } else {
                        upSqlString.append(StringUtils.format(" AND {}.system_id = {} AND {}.site_id = {} AND {}.tenant_id = {} AND {}.del_flag = 0", SWueAlias, baseEntity.getSystemId(), SWueAlias, baseEntity.getSiteId(), SWueAlias, enterprise.getEnterpriseId(), SWueAlias));
                    }
                } else if (StringUtils.isNotBlank(SWLueAlias)) {
                    if (SWLueAlias.equals("empty")) {
                        upSqlString.append(StringUtils.format(" AND system_id = {} AND site_id = {} AND library_id = {} AND tenant_id = {} AND del_flag = 0", baseEntity.getSystemId(), baseEntity.getSiteId(), baseEntity.getLibraryId(), enterprise.getEnterpriseId()));
                    } else {
                        upSqlString.append(StringUtils.format(" AND {}.system_id = {} AND {}.site_id = {} AND {}.library_id = {} AND {}.tenant_id = {} AND {}.del_flag = 0", SWLueAlias, baseEntity.getSystemId(), SWLueAlias, baseEntity.getSiteId(), SWLueAlias, baseEntity.getLibraryId(), SWLueAlias, enterprise.getEnterpriseId(), SWLueAlias));
                    }
                }
            }
            //当无复杂查询控制时，阻断查询进行，保证数据安全
            else {
                sqlString.append(" 1 = 0 ");
                upSqlString.append(" 1 = 0 ");
            }
        }
        //4.当无简单查询控制 || 基类为空时，阻断查询进行，保证数据安全
        else {
            sqlString.append(" 1 = 0 ");
            upSqlString.append(" 1 = 0 ");
        }

        if (StringUtils.isNotBlank(sqlString.toString()) || StringUtils.isNotBlank(upSqlString.toString())) {
            if (StringUtils.isNotNull(params) && params instanceof BaseEntity) {
                BaseEntity baseEntity = (BaseEntity) params;
                baseEntity.setEnterpriseId(enterprise.getEnterpriseId());
                if (StringUtils.isNotBlank(sqlString.toString())) {
                    baseEntity.getParams().put(DATA_SCOPE, " AND (" + sqlString.substring(4) + ")");
                }
                if (StringUtils.isNotBlank(upSqlString.toString())) {
                    //雪花ID生成
                    //终端ID
                    int workerId = RandomUtil.randomInt(0, 31);
                    //数据中心ID
                    int datacenterId = 1;
                    Snowflake snowflake = IdUtil.getSnowflake(workerId, datacenterId);
                    Long id = snowflake.nextId();
                    baseEntity.setId(id);
                    baseEntity.setCreateBy(user.getUserId());
                    baseEntity.setUpdateBy(user.getUserId());
                    baseEntity.getParams().put(UPDATE_SCOPE, " AND (" + upSqlString.substring(4) + ")");
                }
            }
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private DataScope getAnnotationLog(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(DataScope.class);
        }
        return null;
    }
}
