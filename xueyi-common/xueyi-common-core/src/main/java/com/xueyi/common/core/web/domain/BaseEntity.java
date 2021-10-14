package com.xueyi.common.core.web.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xueyi.common.core.annotation.Excel;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Entity基类
 *
 * @author xueyi
 */
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 租户Id */
    private Long enterpriseId;

    /** 系统Id */
    private Long systemId;

    /** 站点Id */
    private Long siteId;

    /** 库Id */
    private Long libraryId;

    /** 雪花Id */
    private Long snowflakeId;

    /** 搜索值 */
    private String searchValue;

    /** 系统默认（Y是 N否） */
    private String isChange;

    /** 更新类型（0 正常更新 1 状态更新） */
    private String updateType;

    /** 数据源名称 */
    private String sourceName;

    /** 显示顺序 */
    private Integer sort;

    /** 创建者Id */
    private Long createBy;

    /** 创建者 */
    private String createName;

    /** 创建者部门Id */
    private Long createDeptBy;

    /** 创建者部门 */
    private String createDeptName;

    /** 创建者岗位Id */
    private Long createPostBy;

    /** 创建者岗位 */
    private String createPostName;

    /** 创建者IP */
    private String createIP;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /** 更新者Id */
    private Long updateBy;

    /** 更新者 */
    private String updateName;

    /** 更新者部门Id */
    private Long updateDeptBy;

    /** 更新者部门 */
    private String updateDeptName;

    /** 更新者岗位Id */
    private Long updatePostBy;

    /** 更新者岗位 */
    private String updatePostName;

    /** 更新者IP */
    private String updateIP;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    /** 删除标志 */
    private Long delFlag;

    /** 请求参数 */
    private Map<String, Object> params;

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public Long getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(Long libraryId) {
        this.libraryId = libraryId;
    }

    public Long getSnowflakeId() {
        return snowflakeId;
    }

    public void setSnowflakeId(Long snowflakeId) {
        this.snowflakeId = snowflakeId;
    }

    public String getSearchValue()
    {
        return searchValue;
    }

    public void setSearchValue(String searchValue)
    {
        this.searchValue = searchValue;
    }

    public String getIsChange() {
        return isChange;
    }

    public void setIsChange(String isChange) {
        this.isChange = isChange;
    }

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public Long getCreateDeptBy() {
        return createDeptBy;
    }

    public void setCreateDeptBy(Long createDeptBy) {
        this.createDeptBy = createDeptBy;
    }

    public String getCreateDeptName() {
        return createDeptName;
    }

    public void setCreateDeptName(String createDeptName) {
        this.createDeptName = createDeptName;
    }

    public Long getCreatePostBy() {
        return createPostBy;
    }

    public void setCreatePostBy(Long createPostBy) {
        this.createPostBy = createPostBy;
    }

    public String getCreatePostName() {
        return createPostName;
    }

    public void setCreatePostName(String createPostName) {
        this.createPostName = createPostName;
    }

    public String getCreateIP() {
        return createIP;
    }

    public void setCreateIP(String createIP) {
        this.createIP = createIP;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public Long getUpdateDeptBy() {
        return updateDeptBy;
    }

    public void setUpdateDeptBy(Long updateDeptBy) {
        this.updateDeptBy = updateDeptBy;
    }

    public String getUpdateDeptName() {
        return updateDeptName;
    }

    public void setUpdateDeptName(String updateDeptName) {
        this.updateDeptName = updateDeptName;
    }

    public Long getUpdatePostBy() {
        return updatePostBy;
    }

    public void setUpdatePostBy(Long updatePostBy) {
        this.updatePostBy = updatePostBy;
    }

    public String getUpdatePostName() {
        return updatePostName;
    }

    public void setUpdatePostName(String updatePostName) {
        this.updatePostName = updatePostName;
    }

    public String getUpdateIP() {
        return updateIP;
    }

    public void setUpdateIP(String updateIP) {
        this.updateIP = updateIP;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Size(min = 0, max = 500, message = "备注不能超过500个字符")
    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public Long getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Long delFlag) {
        this.delFlag = delFlag;
    }

    public Map<String, Object> getParams()
    {
        if (params == null)
        {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params)
    {
        this.params = params;
    }
}