package com.xueyi.system.api.model;

import cn.hutool.core.collection.CollUtil;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 数据权限
 *
 * @author xueyi
 */
public class DataScope implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 数据范围（1全部数据权限 2自定数据权限 3本部门数据权限 4本部门及以下数据权限 5本岗位数据权限  6仅本人数据权限） */
    private String dataScope;

    /** 权限控制 - 部门 */
    private Set<Long> deptScope;

    /** 权限控制 - 岗位 */
    private Set<Long> postScope;

    /** 权限控制 - 用户 */
    private Set<Long> userScope;

    public String getDataScope() {
        return dataScope;
    }

    public void setDataScope(String dataScope) {
        this.dataScope = dataScope;
    }

    public Set<Long> getDeptScope() {
        return CollUtil.isEmpty(deptScope) ? new HashSet<>() : deptScope;
    }

    public void setDeptScope(Set<Long> deptScope) {
        this.deptScope = deptScope;
    }

    public Set<Long> getPostScope() {
        return CollUtil.isEmpty(postScope) ? new HashSet<>() : postScope;
    }

    public void setPostScope(Set<Long> postScope) {
        this.postScope = postScope;
    }

    public Set<Long> getUserScope() {
        return CollUtil.isEmpty(userScope) ? new HashSet<>() : userScope;
    }

    public void setUserScope(Set<Long> userScope) {
        this.userScope = userScope;
    }
}
