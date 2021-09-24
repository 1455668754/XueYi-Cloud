package com.xueyi.common.socket.domain;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 双向HashMap， 可以实现O(1) 按值/键 查找、添加、删除元素对
 *
 * @author xueyi
 */
public class BiDirectionHashMap<K, V, S> {
    private final ConcurrentHashMap<K, ConcurrentHashMap<V, V>> k2v; // key -> value
    private final ConcurrentHashMap<V, K> v2k; // value -> key
    private final ConcurrentHashMap<V, S> v2s; // value -> session

    /**
     * 默认构造函数
     */
    BiDirectionHashMap() {
        this.k2v = new ConcurrentHashMap<>();
        this.v2k = new ConcurrentHashMap<>();
        this.v2s = new ConcurrentHashMap<>();
    }

    /**
     * 添加
     *
     * @param k 键
     * @param v 值
     */
    public void put(K k, V v, S s) {
        if (containsKey(k)) {
            getValueByKey(k).put(v, v);
        } else {
            ConcurrentHashMap<V, V> v2v = new ConcurrentHashMap<>();
            v2v.put(v, v);
            k2v.put(k, v2v);
        }
        v2k.put(v, k);
        v2s.put(v,s);
    }

    /**
     * 查看大小
     *
     * @return 大小
     */
    public int size() {
        return k2v.size();
    }

    /**
     * 查看连接量
     *
     * @return 连接量
     */
    public int linkSize() {
        return v2s.size();
    }

    /**
     * 是否有键
     *
     * @param k 键
     * @return 状态
     */
    public boolean containsKey(K k) {
        return k2v.containsKey(k);
    }

    /**
     * 是否有Value
     *
     * @param v 值
     * @return 状态
     */
    public boolean containsValue(V v) {
        return v2k.containsKey(v);
    }

    /**
     * 通过键删除
     *
     * @param k 键
     * @return 状态
     */
    public boolean removeByKey(K k) {
        if (!k2v.containsKey(k)) {
            return false;
        }
        k2v.get(k).forEach((u, v) -> {v2k.remove(u); v2s.remove(u);});
        k2v.remove(k);
        return true;
    }

    /**
     * 通过值删除
     *
     * @param v 值
     * @return 状态
     */
    public boolean removeByValue(V v) {
        if (!v2k.containsKey(v)) {
            return false;
        }
        K key = v2k.get(v);
        k2v.get(key).remove(v);
        if (k2v.get(key).isEmpty()) {
            k2v.remove(key);
        }
        v2k.remove(v);
        v2s.remove(v);
        return true;
    }

    /**
     * 通过键获取值
     *
     * @param k 键
     * @return v 值
     */
    public ConcurrentHashMap<V, V> getValueByKey(K k) {
        return k2v.getOrDefault(k, null);
    }

    /**
     * 通过值获取键
     *
     * @param v 值
     * @return k 键
     */
    public K getKeyByValue(V v) {
        return v2k.getOrDefault(v, null);
    }

    /**
     * 通过值获取Session
     *
     * @param v 值
     * @return s 键
     */
    public S getSessionByValue(V v) {
        return v2s.getOrDefault(v, null);
    }
}