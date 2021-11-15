package com.xueyi.common.core.constant;

/**
 * 通用常量
 *
 * @author xueyi
 */
public class BaseConstants {

    /** 状态 */
    public enum Status {

        NORMAL("0", "正常"), DISABLE("1", "停用"), EXCEPTION("2", "异常");

        private final String code;
        private final String info;

        Status(String code, String info) {
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

    /** 校验返回结果码 */
    public enum Check {

        UNIQUE("0", "唯一"), NOT_UNIQUE("1", "不唯一");

        private final String code;
        private final String info;

        Check(String code, String info) {
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

    /** 系统默认值 */
    public enum Default {

        YES("Y", "是"), NO("N", "否");

        private final String code;
        private final String info;

        Default(String code, String info) {
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

    /** 删除状态 */
    public enum DelFlag {

        NORMAL(0L, "正常"), DELETED(1L, "已删除");

        private final Long code;
        private final String info;

        DelFlag(Long code, String info) {
            this.code = code;
            this.info = info;
        }

        public Long getCode() {
            return code;
        }

        public String getInfo() {
            return info;
        }
    }
}
