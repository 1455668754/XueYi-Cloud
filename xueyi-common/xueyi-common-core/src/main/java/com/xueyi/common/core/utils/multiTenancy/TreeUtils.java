package com.xueyi.common.core.utils.multiTenancy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.exception.base.BaseException;
import com.xueyi.common.core.utils.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * 树结构组装工具类
 *
 * @author xueyi
 */
public class TreeUtils {

    /**
     * 构建树结构
     *
     * @param list          组装列表
     * @param IdName        Id字段名称
     * @param FIdName       父级Id字段名称
     * @param childrenName  children字段名称
     * @param topNode       顶级节点
     * @param killScattered 是否移除无法追溯到顶级节点 (true 是 | false 否)
     * @return 树结构列表
     */
    public static <T> List<T> buildTree(List<T> list, String IdName, String FIdName, String childrenName, Long topNode, boolean killScattered) {
        List<T> returnList = new ArrayList<T>();
        List<Long> tempList = new ArrayList<Long>();
        try {
            if (CollUtil.isNotEmpty(list)) {
                int IdKey = 0;
                for (T vo : list) {
                    if (IdKey == 0)
                        IdKey = checkAttribute(vo, IdName, IdKey);
                    Field Id = depthRecursive(vo, IdKey).getDeclaredField(IdName);
                    Id.setAccessible(true);
                    tempList.add((Long) Id.get(vo));
                }
                int FIdKey = 0;
                for (T vo : list) {
                    if (FIdKey == 0)
                        FIdKey=checkAttribute(vo, FIdName, FIdKey);
                    Field FId = depthRecursive(vo, FIdKey).getDeclaredField(FIdName);
                    FId.setAccessible(true);
                    // 如果是顶级节点, 遍历该父节点的所有子节点
                    if (!tempList.contains((Long) FId.get(vo))) {
                        recursionFn(list, vo, IdName, FIdName, childrenName);
                        returnList.add(vo);
                    }
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if (returnList.isEmpty()) {
            returnList = list;
        }
        if (killScattered) {
            deleteNoTopNode(returnList, FIdName, topNode);
        }
        return returnList;
    }

    /**
     * 检查是否存在属性
     */
    private static <T> int checkAttribute(T t, String fieldName, int key) {
        Class<?> clazz = t.getClass();
        while (Object.class != clazz) {
            key++;
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (StrUtil.equals(fieldName, field.getName())) {
                    return key;
                }
            }
            clazz = clazz.getSuperclass();
        }
        throw new BaseException(StrUtil.format("对象不存在属性{}", fieldName));
    }

    /**
     * 递归泛型深度
     */
    private static <T> Class<?> depthRecursive(T t, int key) {
        Class<?> clazz = t.getClass();
        int i = 1;
        while (i < key) {
            clazz = clazz.getSuperclass();
            i++;
        }
        return clazz;
    }

    /**
     * 递归列表
     */
    private static <T> void recursionFn(List<T> list, T t, String IdName, String FIdName, String childrenName) {
        // 得到子节点列表
        List<T> childList = getChildList(list, t, IdName, FIdName);
        try {
            Field children = depthRecursive(t, checkAttribute(t, childrenName, 0)).getDeclaredField(childrenName);
            children.setAccessible(true);
            children.set(t, childList);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        for (T tChild : childList) {
            if (hasChild(list, tChild, IdName, FIdName)) {
                recursionFn(list, tChild, IdName, FIdName, childrenName);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private static <T> List<T> getChildList(List<T> list, T t, String IdName, String FIdName) {
        List<T> tList = new ArrayList<T>();
        Iterator<T> it = list.iterator();
        try {
            while (it.hasNext()) {
                T n = (T) it.next();
                Field Id = depthRecursive(t, checkAttribute(t, IdName, 0)).getDeclaredField(IdName);
                Id.setAccessible(true);
                Field FId = depthRecursive(n, checkAttribute(n, FIdName, 0)).getDeclaredField(FIdName);
                FId.setAccessible(true);
                if (StringUtils.isNotNull((Long) FId.get(n)) && ((Long) FId.get(n)).longValue() == ((Long) Id.get(t)).longValue()) {
                    tList.add(n);
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return tList;
    }

    /**
     * 判断是否有子节点
     */
    private static <T> boolean hasChild(List<T> list, T t, String IdName, String FIdName) {
        return getChildList(list, t, IdName, FIdName).size() > 0;
    }

    /**
     * 删除无法溯源至顶级节点的值
     */
    private static <T> void deleteNoTopNode(List<T> list, String FIdName, Long topNode) {
        list.removeIf(vo -> {
            try {
                Field FId = depthRecursive(vo, checkAttribute(vo, FIdName, 0)).getDeclaredField(FIdName);
                FId.setAccessible(true);
                return !Objects.equals(FId.get(vo), topNode);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return false;
        });
    }
}