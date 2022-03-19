package com.xueyi.gen.domain.dto;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.gen.domain.po.GenTableColumnPo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 业务字段 数据传输对象
 *
 * @author xueyi
 */
@TableName(value = "gen_table_column", excludeProperty = {"status", "remark", "delFlag"})
public class GenTableColumnDto extends GenTableColumnPo {

    private static final long serialVersionUID = 1L;

    /** 字典名称 */
    @TableField(exist = false)
    private String dictName;

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public boolean isPk() {
        return getPk();
    }

    public boolean isList() {
        return getList();
    }

    public boolean isInsert() {
        return getInsert();
    }

    public boolean isView() {
        return getView();
    }

    public boolean isEdit() {
        return getEdit();
    }

    public boolean isRequired() {
        return getRequired();
    }

    public boolean isQuery() {
        return getQuery();
    }

    public boolean isImport() {
        return getImport();
    }

    public boolean isExport() {
        return getExport();
    }

    public boolean isHide() {
        return getHide();
    }

    public boolean isCover() {
        return getCover();
    }

    public String readConverterExp() {
        String remarks = StrUtil.subBetween(this.getComment(), "（", "）");
        StringBuffer sb = new StringBuffer();
        if (StringUtils.isNotEmpty(remarks)) {
            for (String value : remarks.split(" ")) {
                if (StringUtils.isNotEmpty(value)) {
                    Object startStr = value.subSequence(0, 1);
                    String endStr = value.substring(1);
                    sb.append("").append(startStr).append("=").append(endStr).append(",");
                }
            }
            return sb.deleteCharAt(sb.length() - 1).toString();
        } else {
            return this.getComment();
        }
    }

    public String readNameNoSuffix() {
        return StrUtil.isNotEmpty(this.getComment()) ? this.getComment().replaceAll("(?:\\（)[^\\(\\)]*(?:\\）)", StrUtil.EMPTY) : this.getComment();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("name", getName())
                .append("comment", getComment())
                .append("tableId", getTableId())
                .append("type", getType())
                .append("javaType", getJavaType())
                .append("javaField", getJavaField())
                .append("isPk", getPk())
                .append("isIncrement", getIncrement())
                .append("isRequired", getRequired())
                .append("isInsert", getInsert())
                .append("isEdit", getEdit())
                .append("isList", getList())
                .append("isQuery", getQuery())
                .append("isUnique", getUnique())
                .append("queryType", getQueryType())
                .append("htmlType", getHtmlType())
                .append("dictType", getDictType())
                .toString();
    }
}