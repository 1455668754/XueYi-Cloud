package com.xueyi.common.core.utils.multiTenancy;

import java.lang.reflect.Field;
import java.util.Comparator;

/**
 * 比较器工具类
 *
 * @author xueyi
 */
public class ComparatorUtils<T> implements Comparator<T> {

    public int compare(T t0, T t1) {
        int i = 0;
        try {
            Field fileId0 = t0.getClass().getSuperclass().getDeclaredField("sort");
            Field fileId1 = t0.getClass().getSuperclass().getDeclaredField("sort");
            fileId0.setAccessible(true);
            fileId1.setAccessible(true);
            i = ((Integer) fileId0.get(t0)).compareTo((Integer) fileId1.get(t1));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return i;
    }
}