package com.xueyi.system.api.organize.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xueyi.common.core.annotation.Excel;
import com.xueyi.common.core.web.tenant.base.TBaseEntity;
import com.xueyi.common.core.xss.Xss;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 岗位 持久化对象
 *
 * @author xueyi
 */
public class SysPostPo extends TBaseEntity {

    private static final long serialVersionUID = 1L;

    /** 部门Id */
    @TableField("dept_id")
    private Long deptId;

    /** 岗位编码 */
    @Excel(name = "岗位编码(*)")
    @TableField("code")
    private String code;

    @NotNull(message = "归属部门不能为空")
    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    @Xss(message = "岗位编码不能包含脚本字符")
    @NotBlank(message = "岗位编码不能为空")
    @Size(min = 0, max = 64, message = "岗位编码长度不能超过64个字符")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    @Xss(message = "岗位名称不能包含脚本字符")
    @NotBlank(message = "岗位名称不能为空")
    @Size(min = 0, max = 50, message = "岗位名称长度不能超过50个字符")
    public String getName() {
        return super.getName();
    }
}
