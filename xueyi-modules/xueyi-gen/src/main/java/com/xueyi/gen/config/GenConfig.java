package com.xueyi.gen.config;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 代码生成相关配置
 *
 * @author xueyi
 */
@Component
@ConfigurationProperties(prefix = "gen", ignoreUnknownFields = false)
public class GenConfig {

    /** 作者 */
    public static String author;

    /** ui路径 */
    public static String uiPath;

    /** 自动去除表前缀，默认是false */
    public static boolean autoRemovePre;

    /** 数据库映射 */
    private static DataBase dataBase;

    /** 字段配置 */
    private static Operate operate;

    /** 基类配置 */
    private static Entity entity;

    /** 表前缀（与remove-lists对应） */
    private static String[] dictTypeRemove = {};

    /** 表更替配置 */
    public static List<RemoveItem> removeLists;

    /** 字段配置 */
    public static class DataBase {

        /** 字符串类型 */
        private String[] typeStr = {};

        /** 文本类型 */
        private String[] typeText = {};

        /** 日期类型 */
        private String[] typeDate = {};

        /** 时间类型 */
        private String[] typeTime = {};

        /** 数字类型 */
        private String[] typeNumber = {};

        /** 长数字类型 */
        private String[] typeLong = {};

        /** 浮点类型 */
        private String[] typeFloat = {};

        public String[] getTypeStr() {
            return typeStr;
        }

        public void setTypeStr(String[] typeStr) {
            this.typeStr = typeStr;
        }

        public String[] getTypeText() {
            return typeText;
        }

        public void setTypeText(String[] typeText) {
            this.typeText = typeText;
        }

        public String[] getTypeDate() {
            return typeDate;
        }

        public void setTypeDate(String[] typeDate) {
            this.typeDate = typeDate;
        }

        public String[] getTypeTime() {
            return typeTime;
        }

        public void setTypeTime(String[] typeTime) {
            this.typeTime = typeTime;
        }

        public String[] getTypeNumber() {
            return typeNumber;
        }

        public void setTypeNumber(String[] typeNumber) {
            this.typeNumber = typeNumber;
        }

        public String[] getTypeLong() {
            return typeLong;
        }

        public void setTypeLong(String[] typeLong) {
            this.typeLong = typeLong;
        }

        public String[] getTypeFloat() {
            return typeFloat;
        }

        public void setTypeFloat(String[] typeFloat) {
            this.typeFloat = typeFloat;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                    .append("typeStr", typeStr)
                    .append("typeText", typeText)
                    .append("typeDate", typeDate)
                    .append("typeTime", typeTime)
                    .append("typeNumber", typeNumber)
                    .append("typeLong", typeLong)
                    .append("typeFloat", typeFloat)
                    .toString();
        }
    }

    /** 字段配置 */
    public static class Operate {
        /** 隐藏详情显示 */
        private String[] notView = {};

        /** 隐藏新增显示 */
        private String[] notInsert = {};

        /** 隐藏编辑显示 */
        private String[] notEdit = {};

        /** 隐藏列表显示 */
        private String[] notList = {};

        /** 隐藏查询显示 */
        private String[] notQuery = {};

        /** 隐藏导入显示 */
        private String[] notImport = {};

        /** 隐藏导出显示 */
        private String[] notExport = {};

        public String[] getNotView() {
            return notView;
        }

        public void setNotView(String[] notView) {
            this.notView = notView;
        }

        public String[] getNotInsert() {
            return notInsert;
        }

        public void setNotInsert(String[] notInsert) {
            this.notInsert = notInsert;
        }

        public String[] getNotEdit() {
            return notEdit;
        }

        public void setNotEdit(String[] notEdit) {
            this.notEdit = notEdit;
        }

        public String[] getNotList() {
            return notList;
        }

        public void setNotList(String[] notList) {
            this.notList = notList;
        }

        public String[] getNotQuery() {
            return notQuery;
        }

        public void setNotQuery(String[] notQuery) {
            this.notQuery = notQuery;
        }

        public String[] getNotImport() {
            return notImport;
        }

        public void setNotImport(String[] notImport) {
            this.notImport = notImport;
        }

        public String[] getNotExport() {
            return notExport;
        }

        public void setNotExport(String[] notExport) {
            this.notExport = notExport;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                    .append("notView", notView)
                    .append("notInsert", notInsert)
                    .append("notEdit", notEdit)
                    .append("notList", notList)
                    .append("notQuery", notQuery)
                    .append("notImport", notImport)
                    .append("notExport", notExport)
                    .toString();
        }
    }

    /** 基类配置 */
    public static class Entity {
        /** 必定隐藏字段（前后端均隐藏） */
        private String[] mustHide = {};

        /** 后端基类 */
        private EntityConfig back = new EntityConfig();

        /** 后端基类 */
        private EntityConfig front = new EntityConfig();

        public static class EntityConfig {

            /** 单表基类 */
            private String[] base = {};

            /** 树表基类 */
            private String[] tree = {};

            /** 主子基类 */
            private String[] sub = {};

            /** 租户基类 */
            private String[] tenant = {};

            public String[] getBase() {
                return base;
            }

            public void setBase(String[] base) {
                this.base = base;
            }

            public String[] getTree() {
                return tree;
            }

            public void setTree(String[] tree) {
                this.tree = tree;
            }

            public String[] getSub() {
                return sub;
            }

            public void setSub(String[] sub) {
                this.sub = sub;
            }

            public String[] getTenant() {
                return tenant;
            }

            public void setTenant(String[] tenant) {
                this.tenant = tenant;
            }

            @Override
            public String toString() {
                return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                        .append("base", base)
                        .append("tree", tree)
                        .append("sub", sub)
                        .append("tenant", tenant)
                        .toString();
            }
        }

        public String[] getMustHide() {
            return mustHide;
        }

        public void setMustHide(String[] mustHide) {
            this.mustHide = mustHide;
        }

        public EntityConfig getBack() {
            return back;
        }

        public void setBack(EntityConfig back) {
            this.back = back;
        }

        public EntityConfig getFront() {
            return front;
        }

        public void setFront(EntityConfig front) {
            this.front = front;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                    .append("mustHide", mustHide)
                    .append("back", back)
                    .append("front", front)
                    .toString();
        }
    }

    /** 表更替配置 */
    public static class RemoveItem {

        /** 表前缀(类名不会包含表前缀) */
        private String prefix;

        /** 生成包路径 */
        private String packageName;

        /** 生成前端包路径 */
        private String frontPackageName;

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getFrontPackageName() {
            return frontPackageName;
        }

        public void setFrontPackageName(String frontPackageName) {
            this.frontPackageName = frontPackageName;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                    .append("prefix", prefix)
                    .append("packageName", packageName)
                    .append("frontPackageName", frontPackageName)
                    .toString();
        }
    }

    public static String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        GenConfig.author = author;
    }

    public static String getUiPath() {
        return uiPath;
    }

    public void setUiPath(String uiPath) {
        GenConfig.uiPath = uiPath;
    }

    public static boolean isAutoRemovePre() {
        return autoRemovePre;
    }

    public void setAutoRemovePre(boolean autoRemovePre) {
        GenConfig.autoRemovePre = autoRemovePre;
    }

    public static DataBase getDataBase() {
        return dataBase;
    }

    public void setDataBase(DataBase dataBase) {
        GenConfig.dataBase = dataBase;
    }

    public static Operate getOperate() {
        return operate;
    }

    public void setOperate(Operate operate) {
        GenConfig.operate = operate;
    }

    public static Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        GenConfig.entity = entity;
    }

    public static String[] getDictTypeRemove() {
        return dictTypeRemove;
    }

    public void setDictTypeRemove(String[] dictTypeRemove) {
        GenConfig.dictTypeRemove = dictTypeRemove;
    }

    public static List<RemoveItem> getRemoveLists() {
        return removeLists;
    }

    public void setRemoveLists(List<RemoveItem> removeLists) {
        GenConfig.removeLists = removeLists;
    }
}
