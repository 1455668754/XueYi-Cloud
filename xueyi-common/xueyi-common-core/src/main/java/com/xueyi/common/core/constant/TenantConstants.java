package com.xueyi.common.core.constant;

/**
 * 租管通用常量
 *
 * @author xueyi
 */
public class TenantConstants {

    /** 注册租户默认策略Id */
    public static final Long REGISTER_TENANT_STRATEGY_ID = 1L;

    /** 策略库 */
    public static final String ISOLATE = "#isolate";

    /** 主库 */
    public static final String MASTER = "#main";

    /** 数据源 */
    public enum Source {

        MAIN("master", "默认数据源"), SLAVE("slave", "从数据源"), REGISTER("slave", "注册数据源");

        private final String code;
        private final String info;

        Source(String code, String info) {
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

    /** 数据源类型 */
    public enum SourceType {

        READ_WRITE("0", "读写"), READ("1", "只读"), WRITE("2", "只写");

        private final String code;
        private final String info;

        SourceType(String code, String info) {
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

    /** 源同步策略类型 */
    public enum SyncType {

        UNCHANGED("0", "不变"), REFRESH("1", "刷新"), ADD("2", "新增"), DELETE("2", "删除");

        private final String code;
        private final String info;

        SyncType(String code, String info) {
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

    /** 主数据源 */
    public enum IsMain {

        YES("Y", "是"), NO("N", "否");

        private final String code;
        private final String info;

        IsMain(String code, String info) {
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
