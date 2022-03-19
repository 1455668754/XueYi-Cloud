package com.xueyi.common.log.enums;

/**
 * 操作状态
 *
 * @author xueyi
 */
public enum BusinessStatus {

    SUCCESS("0", "成功"), FAIL("1", "失败");

    private final String code;
    private final String info;

    BusinessStatus(String code, String info) {
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
