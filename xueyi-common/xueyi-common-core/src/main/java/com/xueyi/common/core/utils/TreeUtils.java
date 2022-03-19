package com.xueyi.common.core.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.exception.base.BaseException;

import java.io.Serializable;
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
     * 存在默认参数 详见BaseConstants.Entity
     * IdName = ID | FIdName = PARENT_ID | childrenName = CHILDREN | topNode = TOP_NODE | killScattered = false | killNoneChild = true
     *
     * @param list 组装列表
     * @return 树结构列表
     */
    public static <T> List<T> buildTree(List<T> list) {
        return buildTree(list, BaseConstants.Entity.ID.getCode(),
                BaseConstants.Entity.PARENT_ID.getCode(),
                BaseConstants.Entity.CHILDREN.getCode(),
                BaseConstants.TOP_ID,
                false,
                true);
    }

    /**
     * 构建树结构
     * 存在默认参数 详见BaseConstants.Entity
     * IdName = ID | FIdName = PARENT_ID | childrenName = CHILDREN | topNode = TOP_NODE | killNoneChild = true
     *
     * @param list          组装列表
     * @param killScattered 是否移除无法追溯到顶级节点 (true 是 | false 否)
     * @return 树结构列表
     */
    public static <T> List<T> buildTree(List<T> list, boolean killScattered) {
        return buildTree(list, BaseConstants.Entity.ID.getCode(),
                BaseConstants.Entity.PARENT_ID.getCode(),
                BaseConstants.Entity.CHILDREN.getCode(),
                BaseConstants.TOP_ID,
                killScattered,
                true);
    }

    /**
     * 构建树结构
     * 存在默认参数 详见BaseConstants.Entity
     * IdName = ID | FIdName = PARENT_ID | childrenName = CHILDREN | topNode = TOP_NODE
     *
     * @param list          组装列表
     * @param killScattered 是否移除无法追溯到顶级节点 (true 是 | false 否)
     * @param killNoneChild 是否移除空子节点集合
     * @return 树结构列表
     */
    public static <T> List<T> buildTree(List<T> list, boolean killScattered, boolean killNoneChild) {
        return buildTree(list, BaseConstants.Entity.ID.getCode(),
                BaseConstants.Entity.PARENT_ID.getCode(),
                BaseConstants.Entity.CHILDREN.getCode(),
                BaseConstants.TOP_ID,
                killScattered,
                killNoneChild);
    }

    /**
     * 构建树结构
     * 存在默认参数 详见BaseConstants.Entity
     * IdName = ID | FIdName = PARENT_ID | childrenName = CHILDREN | killNoneChild = true
     *
     * @param list          组装列表
     * @param topNode       顶级节点
     * @param killScattered 是否移除无法追溯到顶级节点 (true 是 | false 否)
     * @return 树结构列表
     */
    public static <T> List<T> buildTree(List<T> list, Serializable topNode, boolean killScattered) {
        return buildTree(list, BaseConstants.Entity.ID.getCode(),
                BaseConstants.Entity.PARENT_ID.getCode(),
                BaseConstants.Entity.CHILDREN.getCode(),
                topNode,
                killScattered,
                true);
    }

    /**
     * 构建树结构
     *
     * @param list          组装列表
     * @param IdName        Id字段名称
     * @param FIdName       父级Id字段名称
     * @param childrenName  children字段名称
     * @param topNode       顶级节点
     * @param killScattered 是否移除无法追溯到顶级节点 (true 是 | false 否)
     * @param killNoneChild 是否移除空子节点集合
     * @return 树结构列表
     */
    public static <T> List<T> buildTree(List<T> list, String IdName, String FIdName, String childrenName, Serializable topNode, boolean killScattered, boolean killNoneChild) {
        List<T> returnList = new ArrayList<>();
        List<Long> tempList = new ArrayList<>();
        T top = null;
        boolean hasTopNode = false;
        try {
            if (CollUtil.isNotEmpty(list)) {
                int IdKey = 0;
                for (T vo : list) {
                    if (IdKey == 0)
                        IdKey = checkAttribute(vo, IdName, IdKey);
                    Field Id = depthRecursive(vo, IdKey).getDeclaredField(IdName);
                    Id.setAccessible(true);
                    if (ObjectUtil.equal(Id.get(vo), topNode)) {
                        hasTopNode = true;
                        top = vo;
                        list.remove(vo);
                        break;
                    }
                }
                for (T vo : list) {
                    Field Id = depthRecursive(vo, IdKey).getDeclaredField(IdName);
                    Id.setAccessible(true);
                    tempList.add((Long) Id.get(vo));
                }
                int FIdKey = 0;
                for (T vo : list) {
                    if (FIdKey == 0)
                        FIdKey = checkAttribute(vo, FIdName, FIdKey);
                    Field FId = depthRecursive(vo, FIdKey).getDeclaredField(FIdName);
                    FId.setAccessible(true);
                    // 如果是顶级节点, 遍历该父节点的所有子节点
                    if (!tempList.contains((Long) FId.get(vo))) {
                        recursionFn(list, vo, IdName, FIdName, childrenName, killNoneChild);
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
        if (hasTopNode && ObjectUtil.isNotNull(top)) {
            List<T> topList = new ArrayList<>();
            try {
                Field children = depthRecursive(top, checkAttribute(top, childrenName, 0)).getDeclaredField(childrenName);
                children.setAccessible(true);
                if (killNoneChild) {
                    if (CollUtil.isNotEmpty(returnList))
                        children.set(top, returnList);
                } else {
                    children.set(top, returnList);
                }
                topList.add(top);
                return topList;
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
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
    private static <T> void recursionFn(List<T> list, T t, String IdName, String FIdName, String childrenName, boolean killNoneChild) {
        // 得到子节点列表
        List<T> childList = getChildList(list, t, IdName, FIdName);
        try {
            Field children = depthRecursive(t, checkAttribute(t, childrenName, 0)).getDeclaredField(childrenName);
            children.setAccessible(true);
            if (killNoneChild) {
                if (CollUtil.isNotEmpty(childList))
                    children.set(t, childList);
            } else {
                children.set(t, childList);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        for (T tChild : childList) {
            if (hasChild(list, tChild, IdName, FIdName)) {
                recursionFn(list, tChild, IdName, FIdName, childrenName, killNoneChild);
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
                if (ObjectUtil.isNotNull((Long) FId.get(n)) && ((Long) FId.get(n)).longValue() == ((Long) Id.get(t)).longValue()) {
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
    private static <T> void deleteNoTopNode(List<T> list, String FIdName, Serializable topNode) {
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

    /**
     * 获取树结构叶子节点集合
     * 存在默认参数 详见BaseConstants.Entity
     * childrenName = CHILDREN
     *
     * @param list 组装列表
     * @return 叶子节点集合
     */
    public static <T> List<T> getLeafNodes(List<T> list) {
        return getLeafNodes(list, BaseConstants.Entity.CHILDREN.getCode());
    }

    /**
     * 获取树结构叶子节点集合
     * 存在默认参数 详见BaseConstants.Entity
     *
     * @param list         组装列表
     * @param childrenName children字段名称
     * @return 叶子节点集合
     */
    public static <T> List<T> getLeafNodes(List<T> list, String childrenName) {
        List<T> returnList = new ArrayList<>();
        try {
            if (CollUtil.isNotEmpty(list)) {
                T t = list.stream().findFirst().orElse(null);
                assert t != null;
                int childrenKey = checkAttribute(t, childrenName, 0);
                recursionLeafFn(returnList, list, childrenName, childrenKey);
            }
        } catch (Exception ignored) {

        }
        return returnList;
    }

    /**
     * 递归列表
     */
    private static <T> void recursionLeafFn(List<T> returnList, List<T> list, String childrenName, int childrenKey) {
        for (T vo : list) {
            try {
                Field children = depthRecursive(vo, childrenKey).getDeclaredField(childrenName);
                children.setAccessible(true);
                List<T> childList = (List<T>) children.get(vo);
                if (CollUtil.isEmpty(childList)) {
                    returnList.add(vo);
                } else {
                    recursionLeafFn(returnList, childList, childrenName, childrenKey);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}