package com.xueyi.common.core.constant.system;

import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.basic.BaseConstants;

/**
 * 权限通用常量
 *
 * @author xueyi
 */
public class AuthorityConstants {

    /** 菜单树 - 顶级节点Id */
    public static final Long MENU_TOP_NODE = BaseConstants.TOP_ID;

    /** 默认模块Id */
    public static final Long MODULE_DEFAULT_NODE = 1L;

    /** 用户类型 */
    public enum UserType {

        NORMAL("01", "普通用户"),
        ADMIN("00", "超管用户");

        private final String code;
        private final String info;

        UserType(String code, String info) {
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

    /** 租户类型 */
    public enum TenantType {

        NORMAL("N", "普通租户"),
        ADMIN("Y", "租管租户");

        private final String code;
        private final String info;

        TenantType(String code, String info) {
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

    /** 数据范围 */
    public enum DataScope {

        NONE("0","无数据权限"),
        ALL("1","全部数据权限"),
        CUSTOM("2","自定义数据权限"),
        DEPT("3","本部门数据权限"),
        DEPT_AND_CHILD("4","本部门及以下数据权限"),
        POST("5","本岗位数据权限"),
        SELF("6","仅本人数据权限");

        private final String code;
        private final String info;

        DataScope(String code, String info) {
            this.code = code;
            this.info = info;
        }

        public String getCode() {
            return code;
        }

        public String getInfo() {
            return info;
        }

        public static DataScope getValue(String code){
            for(DataScope scope : values()){
                if(StrUtil.equals(code, scope.getCode())){
                    return scope;
                }
            }
            return null;
        }
    }

    /** 页面类型 */
    public enum FrameType {

        NORMAL("0", "正常"),
        EMBEDDED("1", "内嵌"),
        EXTERNAL_LINKS("2", "外链");

        private final String code;
        private final String info;

        FrameType(String code, String info) {
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

    /** 菜单类型 */
    public enum MenuType {

        DIR("M", "目录"),
        MENU("C", "菜单"),
        DETAILS("X", "详情"),
        BUTTON("F", "按钮");

        private final String code;
        private final String info;

        MenuType(String code, String info) {
            this.code = code;
            this.info = info;
        }

        public String getCode() {
            return code;
        }

        public String getInfo() {
            return info;
        }

        public static MenuType getValue(String code) {
            for (MenuType one : values())
                if (StrUtil.equals(code, one.getCode()))
                    return one;
            return null;
        }
    }

    /** 组件标识 */
    public enum ComponentType {

        LAYOUT("LAYOUT", "Layout"),
        PARENT_VIEW("ParentView", "ParentView"),
        IFRAME("IFrame", "IFrame");

        private final String code;
        private final String info;

        ComponentType(String code, String info) {
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

    /** 权限类型 */
    public enum AuthorityType {

        MODULE("0", "模块"),
        MENU("1", "菜单");

        private final String code;
        private final String info;

        AuthorityType(String code, String info) {
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