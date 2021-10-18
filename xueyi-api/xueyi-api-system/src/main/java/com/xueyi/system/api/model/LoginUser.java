package com.xueyi.system.api.model;

import com.xueyi.system.api.domain.organize.SysEnterprise;
import com.xueyi.system.api.domain.organize.SysUser;
import com.xueyi.system.api.domain.source.Source;

import java.io.Serializable;
import java.util.Set;

/**
 * 用户信息
 *
 * @author xueyi
 */
public class LoginUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 用户唯一标识 */
    private String token;

    /** 企业账号Id */
    private Long enterpriseId;

    /** 企业账号 */
    private String enterpriseName;

    /** 租户标识 */
    private String isLessor;

    /** 用户名Id */
    private Long userId;

    /** 用户名 */
    private String userName;

    /** 用户标识 */
    private String userType;

    /** 登录时间 */
    private Long loginTime;

    /** 过期时间 */
    private Long expireTime;

    /** 登录IP地址 */
    private String ipaddr;

    /** 权限列表 */
    private Set<String> permissions;

    /** 角色权限列表 */
    private Set<String> roles;

    /** 角色列表 */
    private Set<Long> roleIds;

    /** 主数据源 */
    private String mainSource;

    /** 源策略组 */
    private Source source;

    /** 企业信息 */
    private SysEnterprise sysEnterprise;

    /** 用户信息 */
    private SysUser sysUser;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getIsLessor() {
        return isLessor;
    }

    public void setIsLessor(String isLessor) {
        this.isLessor = isLessor;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public String getIpaddr() {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Set<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public String getMainSource() {
        return mainSource;
    }

    public void setMainSource(String mainSource) {
        this.mainSource = mainSource;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public SysEnterprise getSysEnterprise() {
        return sysEnterprise;
    }

    public void setSysEnterprise(SysEnterprise sysEnterprise) {
        this.sysEnterprise = sysEnterprise;
    }

    public SysUser getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }
}
