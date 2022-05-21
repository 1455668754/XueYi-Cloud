package com.xueyi.common.web.enums;

public enum SqlMethod {
    INSERT_BATCH("insertBatch", "批量插入数据", "<script>\nINSERT INTO %s %s VALUES %s\n</script>"),
    UPDATE_BATCH("updateBatch", "批量根据ID修改数据", "<script>\n<foreach collection=\"collection\" item=\"item\" separator=\";\">\nupdate %s %s where %s=#{%s} %s\n</foreach>\n</script>");

    private final String method;
    private final String desc;
    private final String sql;

    SqlMethod(String method, String desc, String sql) {
        this.method = method;
        this.desc = desc;
        this.sql = sql;
    }

    public String getMethod() {
        return this.method;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getSql() {
        return this.sql;
    }
}