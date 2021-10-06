package com.xueyi.system.api.domain.authority;

import com.xueyi.common.core.annotation.Excel;
import com.xueyi.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * 角色表 sys_role
 *
 * @author xueyi
 */
public class SysRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 角色Id */
    private Long roleId;

    /** 岗位编码 */
    private String roleCode;

    /** 角色名称 */
    @Excel(name = "角色名称")
    private String name;

    /** 角色权限 */
    @Excel(name = "角色权限")
    private String roleKey;

    /** 数据范围（1：全部数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限 5：本岗位数据权限 6：仅本人数据权限） */
    @Excel(name = "数据范围", readConverterExp = "1=全部数据权限,2=自定义数据权限,3=本部门数据权限,4=本部门及以下数据权限,5=本岗位数据权限,6=仅本人数据权限")
    private String dataScope;

    /** 角色类型（0 常规 1 租户衍生 2 企业衍生 3 部门衍生 4 岗位衍生 5 用户衍生） */
    @Excel(name = "角色类型", readConverterExp = "0=常规,1=租户衍生,2=企业衍生, 3=部门衍生,4=岗位衍生,5=用户衍生")
    private String type;

    /** 衍生Id */
    private Long deriveId;

    /** 角色状态（0正常 1停用） */
    @Excel(name = "角色状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 用户是否存在此角色标识 默认不存在 */
    private boolean flag = false;

    /** 模块-菜单组（全选菜单权限） */
    private Set<SystemMenu> halfIds;

    /** 模块-菜单组（半选菜单权限） */
    private Set<SystemMenu> wholeIds;

    /** 部门-岗位组（数据权限） */
    private Set<Long> deptPostIds;

    public SysRole() {

    }

    public SysRole(Long roleId) {
        this.roleId = roleId;
    }

    public SysRole(String type, Long deriveId, Long enterpriseId) {
        this.type = type;
        this.deriveId = deriveId;
        this.setEnterpriseId(enterpriseId);
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    @NotBlank(message = "角色名称不能为空")
    @Size(min = 0, max = 30, message = "角色名称长度不能超过30个字符")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDeriveId() {
        return deriveId;
    }

    public void setDeriveId(Long deriveId) {
        this.deriveId = deriveId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @NotBlank(message = "权限字符不能为空")
    @Size(min = 0, max = 100, message = "权限字符长度不能超过100个字符")
    public String getRoleKey() {
        return roleKey;
    }

    public void setRoleKey(String roleKey) {
        this.roleKey = roleKey;
    }

    public String getDataScope() {
        return dataScope;
    }

    public void setDataScope(String dataScope) {
        this.dataScope = dataScope;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Set<SystemMenu> getHalfIds() {
        return halfIds;
    }

    public void setHalfIds(Set<SystemMenu> halfIds) {
        this.halfIds = halfIds;
    }

    public Set<SystemMenu> getWholeIds() {
        return wholeIds;
    }

    public void setWholeIds(Set<SystemMenu> wholeIds) {
        this.wholeIds = wholeIds;
    }

    public Set<Long> getDeptPostIds() {
        return deptPostIds;
    }

    public void setDeptPostIds(Set<Long> deptPostIds) {
        this.deptPostIds = deptPostIds;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("roleId", getRoleId())
                .append("roleCode", getRoleCode())
                .append("name", getName())
                .append("roleKey", getRoleKey())
                .append("dataScope", getDataScope())
                .append("type", getType())
                .append("sort", getSort())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createName", getCreateName())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateName", getUpdateName())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .append("deptPostIds", getDeptPostIds())
                .append("halfIds", getHalfIds())
                .append("wholeIds", getWholeIds())
                .toString();
    }
}