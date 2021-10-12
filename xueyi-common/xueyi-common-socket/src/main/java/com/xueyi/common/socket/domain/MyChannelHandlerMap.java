package com.xueyi.common.socket.domain;

import cn.hutool.crypto.digest.DigestUtil;
import com.xueyi.common.socket.pojo.Session;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * socket 共享
 *
 * @author xueyi
 */
public class MyChannelHandlerMap {

    /**
     * 保存映射关系的双向Hash表
     */
    public static BiDirectionHashMap<String, String, Session> biDirectionHashMap = new BiDirectionHashMap<>();

    /**
     * TODO: 不活跃连接/异常连接清除
     * 记录最后一次通信时间, 用于确定不活跃连接，然后清理掉
     */
    public static ConcurrentHashMap<String, Date> lastUpdate = new ConcurrentHashMap<>();

    /**
     * 是否存在连接
     *
     * @param session 通信
     * @return 状态
     */
    public boolean existConnectionById(Session session) {
        return biDirectionHashMap.containsKey(DigestUtil.md5Hex(session.channel().id().toString()));
    }

    /**
     * 通过session查询用户
     *
     * @param session  通信信息
     */
    public static String getConsumer(Session session) {
        return biDirectionHashMap.getKeyByValue(DigestUtil.md5Hex(session.channel().id().toString()));
    }

    /**
     * 添加
     *
     * @param consumer 用户
     * @param session  通信信息
     */
    public static void put(String consumer, Session session) {
        biDirectionHashMap.put(consumer, DigestUtil.md5Hex(session.channel().id().toString()), session);
        refreshTime(session);
    }

    /**
     * 更新session时间
     *
     * @param session 通信信息
     */
    public static void refreshTime(Session session) {
        lastUpdate.put(DigestUtil.md5Hex(session.channel().id().toString()), new Date());
    }

    /**
     * 根据用户删除
     *
     * @param consumer 用户
     * @return 状态
     */
    public static boolean removeByConsumer(String consumer) {
        biDirectionHashMap.getValueByKey(consumer).forEach((c, s) -> lastUpdate.remove(c));
        return biDirectionHashMap.removeByKey(consumer);
    }

    /**
     * 根据session删除
     *
     * @param session 通信信息
     * @return 状态
     */
    public static boolean removeBySession(Session session) {
        lastUpdate.remove(DigestUtil.md5Hex(session.channel().id().toString()));
        return biDirectionHashMap.removeByValue(DigestUtil.md5Hex(session.channel().id().toString()));
    }

    /**
     * 给一个用户发送一条消息
     *
     * @param consumer 用户
     * @param message 信息
     * @return 状态
     */
    public static boolean sendMessageByConsumer(String consumer, String message) {
        biDirectionHashMap.getValueByKey(consumer).forEach((u,v) -> v.sendText(message));
        return true;
    }


    /**
     * 给多个用户发送一条消息
     *
     * @param consumers 用户集合
     * @param message 信息
     * @return 状态
     */
    public static boolean sendMessageByConsumer(List<String> consumers, String message) {
        consumers.forEach((consumer) -> biDirectionHashMap.getValueByKey(consumer).forEach((u,v) -> v.sendText(message)));
        return true;
    }

    /**
     * 给多个用户发送多条消息（每个用户对应一条消息）
     *
     * @param map 用户 信息
     * @return 状态
     */
    public static boolean sendMessageByConsumer(Map<String,String> map) {
        map.forEach((consumer, message) -> biDirectionHashMap.getValueByKey(consumer).forEach((u,v) -> v.sendText(message)));
        return true;
    }

    /**
     * 查看用户量
     *
     * @return 用户量
     */
    public static int consumerSize() {
        return biDirectionHashMap.size();
    }

    /**
     * 查看连接量
     *
     * @return 连接量
     */
    public static int linkSize() {
        return biDirectionHashMap.linkSize();
    }
}