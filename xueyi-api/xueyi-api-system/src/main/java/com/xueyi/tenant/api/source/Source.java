package com.xueyi.tenant.api.source;

import com.xueyi.common.core.web.domain.BaseEntity;

import java.util.List;

public class Source extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 主库 */
    String master;

    /** 从库列表 */
    List<String> slave;

    /** 类型(0非主数据源 1主数据源) */
    private String isMain;

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public List<String> getSlave() {
        return slave;
    }

    public void setSlave(List<String> slave) {
        this.slave = slave;
    }

    public String getIsMain() {
        return isMain;
    }

    public void setIsMain(String isMain) {
        this.isMain = isMain;
    }
}