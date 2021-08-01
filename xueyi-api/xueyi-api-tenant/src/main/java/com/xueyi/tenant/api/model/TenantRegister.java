package com.xueyi.tenant.api.model;

import java.io.Serializable;

/**
 * 租户信息
 *
 * @author xueyi
 */
public class TenantRegister implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 企业账号Id */
    private Long enterpriseId;

    /** 企业账号 */
    private String enterpriseName;

    /** 系统名称 */
    private String enterpriseSystemName;

    /** 企业昵称 */
    private String enterpriseNick;

    /** 企业logo */
    private String logo;

    /** 用户名Id */
    private Long userId;

    /** 用户名 */
    private String userName;

    /** 用户昵称 */
    private String nickName;

    /** 用户邮箱 */
    private String email;

    /** 手机号码 */
    private String phone;

    /** 用户性别 */
    private String sex;

    /** 用户头像 */
    private String avatar;

    /** 个人简介 */
    private String profile;

    /** 密码 */
    private String password;

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

    public String getEnterpriseSystemName() {
        return enterpriseSystemName;
    }

    public void setEnterpriseSystemName(String enterpriseSystemName) {
        this.enterpriseSystemName = enterpriseSystemName;
    }

    public String getEnterpriseNick() {
        return enterpriseNick;
    }

    public void setEnterpriseNick(String enterpriseNick) {
        this.enterpriseNick = enterpriseNick;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
