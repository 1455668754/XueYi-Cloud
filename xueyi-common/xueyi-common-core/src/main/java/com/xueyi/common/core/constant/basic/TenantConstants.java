package com.xueyi.common.core.constant.basic;

/**
 * 租户通用常量
 *
 * @author xueyi
 */
public class TenantConstants {

    /** 租户字段名 */
    public static final String TENANT_ID = "tenant_id";

    /** 公共字段名 */
    public static final String COMMON_ID = "is_common";

    /** 注册租户默认策略Id */
    public static final Long REGISTER_TENANT_STRATEGY_ID = BaseConstants.COMMON_ID;

    /** 子库必须数据表 */
    public static final String[] SLAVE_TABLE = {"sys_dept", "sys_login_log", "sys_notice", "sys_job_log", "sys_notice_log", "sys_operate_log", "sys_organize_role_merge", "sys_post", "sys_role",
            "sys_role_menu_merge", "sys_role_module_merge", "sys_tenant_menu_merge", "sys_tenant_module_merge", "sys_role_dept_merge", "sys_role_post_merge", "sys_user", "sys_user_post_merge", "xy_material", "xy_material_folder"};

    /** 策略源标识 */
    public static final String ISOLATE = "#isolute";

    /** 主数据源标识 */
    public static final String MASTER = "#master";

    /** 手动数据源标识（调用对象中的sourceName属性） */
    public static final String SOURCE = "#sourceName";

    /** 数据源 */
    public enum Source {

        MASTER("master", "默认数据源"), SLAVE("slave", "从数据源"), REGISTER("slave", "注册数据源");

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

    /** 源同步策略类型 */
    public enum SyncType {

        UNCHANGED("0", "不变"), REFRESH("1", "刷新"), ADD("2", "新增"), DELETE("3", "删除");

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
}
