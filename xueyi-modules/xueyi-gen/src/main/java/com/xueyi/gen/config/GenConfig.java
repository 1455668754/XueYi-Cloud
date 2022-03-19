package com.xueyi.gen.config;

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

    /** 对应配置文件的节点名称 */
    public static List<RemoveItem> removeLists;

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

    public static List<RemoveItem> getRemoveLists() {
        return removeLists;
    }

    public void setRemoveLists(List<RemoveItem> removeLists) {
        GenConfig.removeLists = removeLists;
    }
}
