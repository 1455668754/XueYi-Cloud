package com.xueyi.common.core.constant.basic;

public class SourceConstants {

    /** 数据源查询sql */
    public static final String SELECT_SOURCE = "select s.slave, s.username, s.password, s.url_prepend, s.url_append, s.driver_class_name from te_source s where s.status = 0 and s.del_flag = 0";

    /** 数据源字段 */
    public enum Details {
        SLAVE("slave", "数据源编码"),
        USERNAME("username", "用户名"),
        PASSWORD("password", "密码"),
        URL_PREPEND("url_prepend", "连接地址"),
        URL_APPEND("url_append", "连接参数"),
        DRIVER_CLASS_NAME("driver_class_name", "驱动");

        private final String code;
        private final String info;

        Details(String code, String info) {
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
}
