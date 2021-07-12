package com.xueyi.system.api.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import com.xueyi.system.api.organize.SysEnterprise;
import com.xueyi.system.api.organize.SysUser;
import com.xueyi.system.api.source.Source;

/**
 * 用户信息
 *
 * @author xueyi
 */
public class LoginUser implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 用户唯一标识 */
    private String token;

    /** 企业账号id */
    private Long enterpriseId;

    /** 企业账号 */
    private String enterpriseName;

    /** 用户名id */
    private Long userid;

    /** 用户名 */
    private String username;

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

    /** 角色列表 */
    private Set<String> roles;

    /** 主数据源 */
    private String mainSource;

    /** 数据库列表 */
    private List<Source> source;

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

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getMainSource() {
        return mainSource;
    }

    public void setMainSource(String mainSource) {
        this.mainSource = mainSource;
    }

    public List<Source> getSource() {
        return source;
    }

    public void setSource(List<Source> source) {
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
