package com.xueyi.system.api.domain.authority;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xueyi.common.core.annotation.Excel;
import com.xueyi.common.core.annotation.Excel.ColumnType;
import com.xueyi.common.core.web.domain.BaseEntity;

import java.util.Set;

/**
 * 角色表 sys_role
 *
 * @author xueyi
 */
public class SysRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 角色Id */
    @Excel(name = "角色序号", cellType = ColumnType.NUMERIC)
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

    /** 菜单树选择项是否关联显示（ 0：父子不互相关联显示 1：父子互相关联显示） */
    private boolean menuCheckStrictly;

    /** 部门树选择项是否关联显示（0：父子不互相关联显示 1：父子互相关联显示 ） */
    private boolean deptCheckStrictly;

    /** 角色类型（0常规 1租户衍生 2部门衍生 3岗位衍生 4用户衍生） */
    @Excel(name = "角色类型", readConverterExp = "0=常规,1=租户衍生,2=部门衍生,3=岗位衍生,4=用户衍生")
    private String type;

    /** 衍生Id */
    private Long deriveId;

    /** 角色状态（0正常 1停用） */
    @Excel(name = "角色状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 用户是否存在此角色标识 默认不存在 */
    private boolean flag = false;

    /** 系统-菜单组（菜单权限） */
    private Long[] systemMenuIds;

    /** 系统-菜单组（全选菜单权限） */
    private Set<SysMenu> halfMenuIds;

    /** 系统-菜单组（半选菜单权限） */
    private Set<SysMenu> wholeMenuIds;

    /** 系统-菜单组（全选菜单权限） */
    private Set<SysSystem> halfSystemIds;

    /** 系统-菜单组（半选菜单权限） */
    private Set<SysSystem> wholeSystemIds;

    /** 系统组（角色 - 系统组） */
    private Set<Long> SystemIds;

    /** 部门-岗位组（数据权限） */
    private Long[] deptPostIds;

    public SysRole() {

    }

    public SysRole(Long roleId) {
        this.roleId = roleId;
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

    public boolean isMenuCheckStrictly() {
        return menuCheckStrictly;
    }

    public void setMenuCheckStrictly(boolean menuCheckStrictly) {
        this.menuCheckStrictly = menuCheckStrictly;
    }

    public boolean isDeptCheckStrictly() {
        return deptCheckStrictly;
    }

    public void setDeptCheckStrictly(boolean deptCheckStrictly) {
        this.deptCheckStrictly = deptCheckStrictly;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Long[] getSystemMenuIds() {
        return systemMenuIds;
    }

    public void setSystemMenuIds(Long[] systemMenuIds) {
        this.systemMenuIds = systemMenuIds;
    }

    public Long[] getDeptPostIds() {
        return deptPostIds;
    }

    public void setDeptPostIds(Long[] deptPostIds) {
        this.deptPostIds = deptPostIds;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("roleId", getRoleId())
                .append("roleCode", getRoleCode())
                .append("name", getName())
                .append("roleKey", getRoleKey())
                .append("menuCheckStrictly", isMenuCheckStrictly())
                .append("deptCheckStrictly", isDeptCheckStrictly())
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
                .toString();
    }
}