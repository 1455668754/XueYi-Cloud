package com.xueyi.common.socket.domain;

import java.util.concurrent.ConcurrentHashMap;

/**
 * socket存储类
 *
 * @author xueyi
 */
public class BiDirectionHashMap<K, V, S> {
    private final ConcurrentHashMap<K, ConcurrentHashMap<V, S>> k2v2s; // key -> value
    private final ConcurrentHashMap<V, K> v2k; // value -> key
    private final ConcurrentHashMap<V, S> v2s; // value -> session

    /**
     * 默认构造函数
     */
    BiDirectionHashMap() {
        this.k2v2s = new ConcurrentHashMap<>();
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
            getValueByKey(k).put(v, s);
        } else {
            ConcurrentHashMap<V, S> v2s = new ConcurrentHashMap<>();
            v2s.put(v, s);
            k2v2s.put(k, v2s);
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
        return k2v2s.size();
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
        return k2v2s.containsKey(k);
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
        if (!k2v2s.containsKey(k)) {
            return false;
        }
        k2v2s.get(k).forEach((u, v) -> {v2k.remove(u); v2s.remove(u);});
        k2v2s.remove(k);
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
        k2v2s.get(key).remove(v);
        if (k2v2s.get(key).isEmpty()) {
            k2v2s.remove(key);
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
    public ConcurrentHashMap<V, S> getValueByKey(K k) {
        return k2v2s.getOrDefault(k, null);
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

    /**
     * 检查键是否存在
     *
     * @param k 键
     * @return 结果（true | false）
     */
    public boolean existKey(K k){
        return k2v2s.containsKey(k);
    }

    /**
     * 检查值是否存在
     *
     * @param v 值
     * @return 结果（true | false）
     */
    public boolean existValue(V v){
        return v2k.containsKey(v);
    }
}