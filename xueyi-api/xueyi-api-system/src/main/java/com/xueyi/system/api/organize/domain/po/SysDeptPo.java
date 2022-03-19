package com.xueyi.system.api.organize.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xueyi.common.core.web.tenant.base.TSubTreeEntity;
import com.xueyi.common.core.xss.Xss;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


/**
 * 部门 持久化对象
 *
 * @param <D> Dto
 * @param <S> SubDto
 * @author xueyi
 */
public class SysDeptPo<D, S> extends TSubTreeEntity<D, S> {

    private static final long serialVersionUID = 1L;

    /** 部门编码 */
    @TableField("code")
    private String code;

    /** 负责人 */
    @TableField("leader")
    private String leader;

    /** 联系电话 */
    @TableField("phone")
    private String phone;

    /** 邮箱 */
    @TableField("email")
    private String email;

    @Xss(message = "用户账号不能包含脚本字符")
    @NotBlank(message = "部门编码不能为空")
    @Size(max = 30, message = "部门编码长度不能超过20个字符")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    @Xss(message = "用户账号不能包含脚本字符")
    @NotBlank(message = "部门名称不能为空")
    @Size(max = 30, message = "部门名称长度不能超过30个字符")
    public String getName() {
        return super.getName();
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    @Size(max = 11, message = "联系电话长度不能超过11个字符")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过50个字符")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
