package com.xueyi.common.message.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;

/**
 * 消息确认机制
 *
 * @author Ethereal
 */
public class MessageTemplateConfig implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {
    private static final Logger logger = LoggerFactory.getLogger(MessageTemplateConfig.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }


    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        //发送成功
        if (ack) {
            //不做处理，等待消费成功，清楚缓存
            logger.info("{}：发送成功", correlationData.getId());
        }
        else {
            //持久化到数据库
            logger.error(correlationData.getId() + "{}：发送失败");
            logger.info("备份内容：{}", redisTemplate.opsForValue().get(correlationData.getId()));
        }
        //不管成功与否读删除redis里面备份的数据
        redisTemplate.delete(correlationData.getId());
    }

    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        logger.error("消息体: {}", returnedMessage.getMessage());
        logger.error("描述：{}", returnedMessage.getReplyText());
        logger.error("消息使用的交换器: {}", returnedMessage.getExchange());
        logger.error("消息使用的路由key: {}", returnedMessage.getRoutingKey());
    }
}