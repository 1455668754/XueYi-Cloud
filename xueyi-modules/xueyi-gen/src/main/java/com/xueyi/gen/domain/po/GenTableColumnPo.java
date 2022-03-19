package com.xueyi.gen.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xueyi.common.core.web.entity.base.BaseEntity;

import javax.validation.constraints.NotBlank;

/**
 * 业务字段 持久化对象
 *
 * @author xueyi
 */
public class GenTableColumnPo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 归属表Id */
    @TableField("table_id")
    private Long tableId;

    /** 列描述 */
    @TableField("comment")
    private String comment;

    /** 列类型 */
    @TableField("type")
    private String type;

    /** JAVA类型 */
    @TableField("java_type")
    private String javaType;

    /** JAVA字段名 */
    @NotBlank(message = "Java属性不能为空")
    @TableField("java_field")
    private String javaField;

    /** 主键字段（Y是 N否） */
    @TableField("is_pk")
    private Boolean isPk;

    /** 自增字段（Y是 N否） */
    @TableField("is_increment")
    private Boolean isIncrement;

    /** 必填字段（Y是 N否） */
    @TableField("is_required")
    private Boolean isRequired;

    /** 查看字段（Y是 N否） */
    @TableField("is_view")
    private Boolean isView;

    /** 新增字段（Y是 N否） */
    @TableField("is_insert")
    private Boolean isInsert;

    /** 编辑字段（Y是 N否） */
    @TableField("is_edit")
    private Boolean isEdit;

    /** 列表字段（Y是 N否） */
    @TableField("is_list")
    private Boolean isList;

    /** 查询字段（Y是 N否） */
    @TableField("is_query")
    private Boolean isQuery;

    /** 唯一字段（Y是 N否） */
    @TableField("is_unique")
    private Boolean isUnique;

    /** 导入字段（1是 0否） */
    @TableField("is_import")
    private Boolean isImport;

    /** 导出字段（1是 0否） */
    @TableField("is_export")
    private Boolean isExport;

    /** 隐藏字段（1是 0否） */
    @TableField("is_hide")
    private Boolean isHide;

    /** 覆盖字段（1是 0否） */
    @TableField("is_cover")
    private Boolean isCover;

    /** 查询方式（EQ等于、NE不等于、GT大于、LT小于、LIKE模糊、BETWEEN范围） */
    @TableField("query_type")
    private String queryType;

    /** 显示类型（input文本框、textarea文本域、select下拉框、checkbox复选框、radio单选框、datetime日期控件、image图片上传控件、upload文件上传控件、editor富文本控件） */
    @TableField("html_type")
    private String htmlType;

    /** 字典类型 */
    @TableField("dict_type")
    private String dictType;

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getJavaField() {
        return javaField;
    }

    public void setJavaField(String javaField) {
        this.javaField = javaField;
    }

    public Boolean getPk() {
        return isPk;
    }

    public void setPk(Boolean isPk) {
        this.isPk = isPk;
    }

    public Boolean getIncrement() {
        return isIncrement;
    }

    public void setIncrement(Boolean isIncrement) {
        this.isIncrement = isIncrement;
    }

    public Boolean getRequired() {
        return isRequired;
    }

    public void setRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }

    public Boolean getView() {
        return isView;
    }

    public void setView(Boolean view) {
        isView = view;
    }

    public Boolean getInsert() {
        return isInsert;
    }

    public void setInsert(Boolean isInsert) {
        this.isInsert = isInsert;
    }

    public Boolean getEdit() {
        return isEdit;
    }

    public void setEdit(Boolean isEdit) {
        this.isEdit = isEdit;
    }

    public Boolean getList() {
        return isList;
    }

    public void setList(Boolean isList) {
        this.isList = isList;
    }

    public Boolean getQuery() {
        return isQuery;
    }

    public void setQuery(Boolean isQuery) {
        this.isQuery = isQuery;
    }

    public Boolean getUnique() {
        return isUnique;
    }

    public void setUnique(Boolean isUnique) {
        this.isUnique = isUnique;
    }

    public Boolean getImport() {
        return isImport;
    }

    public void setImport(Boolean anImport) {
        isImport = anImport;
    }

    public Boolean getExport() {
        return isExport;
    }

    public void setExport(Boolean export) {
        isExport = export;
    }

    public Boolean getHide() {
        return isHide;
    }

    public void setHide(Boolean hide) {
        isHide = hide;
    }

    public Boolean getCover() {
        return isCover;
    }

    public void setCover(Boolean cover) {
        isCover = cover;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public String getHtmlType() {
        return htmlType;
    }

    public void setHtmlType(String htmlType) {
        this.htmlType = htmlType;
    }

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

}