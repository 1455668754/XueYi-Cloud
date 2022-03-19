package com.xueyi.common.log.enums;

/**
 * 业务操作类型
 *
 * @author xueyi
 */
public enum BusinessType {

    /** 其它 */
    OTHER("00", "其它"),

    /** 新增 */
    INSERT("01", "新增"),

    /** 强制新增 */
    INSERT_FORCE("02", "强制新增"),

    /** 修改 */
    UPDATE("03", "修改"),

    /** 强制修改 */
    UPDATE_FORCE("04", "强制修改"),

    /** 权限控制 */
    AUTH("05", "权限控制"),

    /** 修改状态 */
    UPDATE_STATUS("06", "修改状态"),

    /** 强制修改状态 */
    UPDATE_STATUS_FORCE("07", "强制修改状态"),

    /** 删除 */
    DELETE("08", "删除"),

    /** 强制删除 */
    DELETE_FORCE("09", "强制删除"),

    /** 授权 */
    GRANT("10", "授权"),

    /** 导出 */
    EXPORT("11", "导出"),

    /** 导入 */
    IMPORT("12", "导入"),

    /** 强退 */
    FORCE("13", "强退"),

    /** 生成代码 */
    GEN_CODE("14", "生成代码"),

    /** 清空数据 */
    CLEAN("15", "清空数据"),

    /** 更新缓存 */
    REFRESH("16", "更新缓存");

    private final String code;
    private final String info;

    BusinessType(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
