package com.xueyi.common.socket.domain;

import cn.hutool.crypto.digest.DigestUtil;
import com.xueyi.common.socket.pojo.Session;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 共享
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
     * @param session 通信
     * @return 状态
     */
    public boolean existConnectionById(Session session) {
        return biDirectionHashMap.containsKey(DigestUtil.md5Hex(session.channel().toString()));
    }

    /**
     * 查看用户量
     *
     * @return 用户量
     */
    public int userSize() {
        return biDirectionHashMap.size();
    }

    /**
     * 查看连接量
     *
     * @return 连接量
     */
    public int linkSize() {
        return biDirectionHashMap.linkSize();
    }

    /**
     * 添加
     *
     * @param user 用户
     * @param session 通信信息
     */
    public void put(String user, Session session) {
        biDirectionHashMap.put(user, DigestUtil.md5Hex(session.channel().toString()), session);
        refreshTime(session);
    }

    /**
     * 更新session时间
     *
     * @param session 通信信息
     */
    public void refreshTime(Session session) {
        lastUpdate.put(DigestUtil.md5Hex(session.channel().toString()), new Date());
    }

    /**
     * 根据用户删除
     *
     * @param user 用户
     * @return 状态
     */
    public boolean removeByUser(String user) {
        biDirectionHashMap.getValueByKey(user).forEach((u,c) -> lastUpdate.remove(u));
        return biDirectionHashMap.removeByKey(user);
    }

    /**
     * 根据用户删除
     *
     * @param session 通信信息
     * @return 状态
     */
    public boolean removeBySession(Session session) {
        lastUpdate.remove(DigestUtil.md5Hex(session.channel().toString()));
        return biDirectionHashMap.removeByValue(DigestUtil.md5Hex(session.channel().toString()));
    }
}