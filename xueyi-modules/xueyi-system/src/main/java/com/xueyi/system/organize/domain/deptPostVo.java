package com.xueyi.system.organize.domain;

import com.xueyi.common.core.web.domain.BaseEntity;
import java.util.ArrayList;
import java.util.List;

/**
 * 部门-岗位数组装结构
 *
 * @author xueyi
 */
public class deptPostVo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** Id */
    private Long Uid;

    /** 父Id（岗位父Id为归属部门） */
    private Long FUid;

    /** 名称 */
    private String name;

    /** 状态 */
    private String status;

    /** 类型（0部门 1岗位） */
    private String type;

    /** 子部门/岗位 */
    private List<deptPostVo> children = new ArrayList<deptPostVo>();

    public Long getUid() {
        return Uid;
    }

    public void setUid(Long uid) {
        Uid = uid;
    }

    public Long getFUid() {
        return FUid;
    }

    public void setFUid(Long FUid) {
        this.FUid = FUid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<deptPostVo> getChildren() {
        return children;
    }

    public void setChildren(List<deptPostVo> children) {
        this.children = children;
    }
}
