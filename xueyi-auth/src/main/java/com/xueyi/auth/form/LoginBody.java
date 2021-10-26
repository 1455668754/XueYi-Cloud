package com.xueyi.auth.form;

/**
 * 用户登录对象
 *
 * @author xueyi
 */
public class LoginBody {

    /** 企业账户 */
    private String enterpriseName;

    /** 用户名 */
    private String userName;

    /** 用户密码 */
    private String password;

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}