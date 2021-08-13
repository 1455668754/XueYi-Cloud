package com.xueyi.common.message.service;

import com.xueyi.common.message.domain.Message;

/**
 * 消息队列-生产者 业务层
 *
 * @author Ethereal
 */
public interface ProducerService
{
    /**
     * 发送消息
     *
     * @param content 消息体
     * @param exchangeName 交换机名称
     * @param routingKey 路由key
     */
    void sendMsg(Message content, String exchangeName, String routingKey);
}