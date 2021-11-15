package com.xueyi.common.core.constant;

import com.xueyi.common.core.utils.StringUtils;

/**
 * 任务调度通用常量
 * 
 * @author xueyi
 */
public class ScheduleConstants {

    public static final String TASK_CLASS_NAME = "TASK_CLASS_NAME";

    /** 执行目标key */
    public static final String TASK_PROPERTIES = "TASK_PROPERTIES";

    /** 任务状态 */
    public enum Status {

        NORMAL("0", "正常"), PAUSE("1", "暂停");

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

    /** 错误策略 */
    public enum Misfire {

        DEFAULT("0", "默认"),
        IGNORE_MISFIRES("1", "立即触发执行"),
        FIRE_AND_PROCEED("2", "触发一次执行"),
        DO_NOTHING("3", "不触发立即执行");

        private final String code;
        private final String info;

        Misfire(String code, String info) {
            this.code = code;
            this.info = info;
        }

        public String getCode() {
            return code;
        }

        public String getInfo() {
            return info;
        }

        public static Misfire getValue(String code){
            for(Misfire misfire : values()){
                if(StringUtils.equals(code, misfire.getCode())){
                    return misfire;
                }
            }
            return null;
        }
    }
}
