package com.xueyi.gen.domain.dto;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.constant.gen.GenConstants;
import com.xueyi.gen.domain.po.GenTablePo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 业务 数据传输对象
 *
 * @author xueyi
 */
@TableName(value = "gen_table", excludeProperty = {"status", "sort", "delFlag"})
public class GenTableDto extends GenTablePo<GenTableColumnDto> {

    private static final long serialVersionUID = 1L;

    /** 主键信息 */
    @TableField(exist = false)
    private GenTableColumnDto pkColumn;

    /** 子表信息 */
    @TableField(exist = false)
    private GenTableDto subTable;

    public static boolean isBase(String tplCategory) {
        return tplCategory != null && StrUtil.equals(GenConstants.TemplateType.BASE.getCode(), tplCategory);
    }

    public static boolean isTree(String tplCategory) {
        return tplCategory != null && StrUtil.equals(GenConstants.TemplateType.TREE.getCode(), tplCategory);
    }

    public static boolean isSubBase(String tplCategory) {
        return tplCategory != null && StrUtil.equals(GenConstants.TemplateType.SUB_BASE.getCode(), tplCategory);
    }

    public static boolean isSubTree(String tplCategory) {
        return tplCategory != null && StrUtil.equals(GenConstants.TemplateType.SUB_TREE.getCode(), tplCategory);
    }

    public static boolean isMerge(String tplCategory) {
        return tplCategory != null && StrUtil.equals(GenConstants.TemplateType.MERGE.getCode(), tplCategory);
    }

    public GenTableColumnDto getPkColumn() {
        return pkColumn;
    }

    public void setPkColumn(GenTableColumnDto pkColumn) {
        this.pkColumn = pkColumn;
    }

    public GenTableDto getSubTable() {
        return subTable;
    }

    public void setSubTable(GenTableDto subTable) {
        this.subTable = subTable;
    }

    /** 是否为单表 */
    public boolean isBase() {
        return isBase(getTplCategory());
    }

    /** 是否为树表 */
    public boolean isTree() {
        return isTree(getTplCategory());
    }

    /** 是否为主子单表 */
    public boolean isSubBase() {
        return isSubBase(getTplCategory());
    }

    /** 是否为主子树表 */
    public boolean isSubTree() {
        return isSubTree(getTplCategory());
    }

    /** 是否为关联表 */
    public boolean isMerge() {
        return isMerge(getTplCategory());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("name", getName())
                .append("comment", getComment())
                .append("className", getClassName())
                .append("prefix", getPrefix())
                .append("tplCategory", getTplCategory())
                .append("packageName", getPackageName())
                .append("moduleName", getModuleName())
                .append("businessName", getBusinessName())
                .append("functionName", getFunctionName())
                .append("functionAuthor", getFunctionAuthor())
                .append("genType", getGenType())
                .append("genPath", getGenPath())
                .append("options", getOptions())
                .append("pkColumn", getPkColumn())
                .append("subTable", getSubTable())
                .toString();
    }
}