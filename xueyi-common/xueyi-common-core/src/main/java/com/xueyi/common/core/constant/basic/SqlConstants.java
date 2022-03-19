package com.xueyi.common.core.constant.basic;

/**
 * sql 通用常量
 *
 * @author xueyi
 */
public class SqlConstants {

    /** sql 查询返回限制 */
    public static final int DEFAULT_BATCH_SIZE = 1000;

    /** sql 查询返回限制 */
    public static final String LIMIT_ONE = "limit 1";

    /** sql find_in_set函数 */
    public static final String ANCESTORS_FIND = "find_in_set({0}, ancestors)";

    /** 数据库字段映射 */
    public enum Entity {

        ID("id", "Id字段"),
        PARENT_ID("parent_id", "父级Id字段"),
        STATUS("status", "状态字段"),
        SORT("sort","排序字段"),
        ANCESTORS("ancestors","祖籍字段");

        private final String code;
        private final String info;

        Entity(String code, String info) {
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
