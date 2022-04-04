package com.xueyi.common.core.web.entity.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xueyi.common.core.web.validate.V_E;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Basis 基类
 *
 * @author xueyi
 */
public class BasisEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Id */
    @TableId("id")
    private Long id;

    /** 数据源名称 */
    @TableField(exist = false)
    private String sourceName;

    /** 请求参数 */
    @TableField(exist = false)
    private Map<String, Object> params;

    @NotEmpty(message = "id不能为空", groups = {V_E.class})
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public Map<String, Object> getParams() {
        return params == null ? new HashMap<>() : params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
