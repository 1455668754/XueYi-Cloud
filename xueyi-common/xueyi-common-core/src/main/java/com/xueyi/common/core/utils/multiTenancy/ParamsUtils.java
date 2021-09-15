package com.xueyi.common.core.utils.multiTenancy;

import java.util.ArrayList;
import java.util.List;

/**
 * BaseEntity 中 params 类型转换工具类
 *
 * @author ruoyi
 */
public class ParamsUtils {

    /**
     *  Object转Long[]
     *  eg: 原：  Object型 [-1, 1, 2, 3] | 来源：前端执行批量删除时Id组存在BaseEntity中的params内
     *      待转：Long[]型
     * @param object Long型数组格式对象
     * @return Long数组
     */
    public static Long[] IdsObjectToLongArray(Object object) {
        String[] temp = object.toString().replaceAll("(?:\\[|\\]| )","").split(",");
        Long[] Ids = new Long[temp.length];
        for (int i = 0; i < temp.length; i++) {
            Ids[i] = Long.valueOf(temp[i]);
        }
        return Ids;
    }

    /**
     *  Object转Long[]
     *  eg: 原：  Object型 [-1, 1, 2, 3] | 来源：前端执行批量删除时Id组存在BaseEntity中的params内
     *      待转：Long[]型
     * @param object List<Long>格式对象
     * @return List<Long>
     */
    public static List<Long> IdsObjectToLongList(Object object) {
        String[] temp = object.toString().replaceAll("(?:\\[|\\]| )","").split(",");
        List<Long> Ids = new ArrayList<Long>();
        for (String s : temp) {
            Ids.add(Long.valueOf(s));
        }
        return Ids;
    }
}
