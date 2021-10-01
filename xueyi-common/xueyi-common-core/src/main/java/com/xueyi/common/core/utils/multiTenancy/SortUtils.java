package com.xueyi.common.core.utils.multiTenancy;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 排序工具类
 *
 * @author xueyi
 */
public class SortUtils {

    /**
     * Set<T>转List<T>
     *
     * @param set 集合
     * @return 结果
     */
    public static <T> List<T> sortSetToList(Set<T> set) {
        List<T> list = new ArrayList<T>(set);
        list.sort(new ComparatorUtils<>());
        return list;
    }
}