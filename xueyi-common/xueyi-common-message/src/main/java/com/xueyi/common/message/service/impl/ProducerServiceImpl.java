package com.xueyi.common.message.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xueyi.common.message.domain.Message;
import com.xueyi.common.message.service.ProducerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 消息队列-生产者 业务层处理
 *
 * @author Ethereal
 */
@Component
public class ProducerServiceImpl implements ProducerService {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void sendMsg(Message content, String exchangeName, String routingKey) {
        org.springframework.amqp.core.Message message = MessageBuilder.withBody(JSONObject.toJSONString(content).getBytes()).setContentType(MessageProperties.CONTENT_TYPE_JSON).setCorrelationId(content.getId()).build();
        if (StringUtils.isNotBlank(content.getExpiration()))
        {
            message = MessageBuilder.fromMessage(message).setExpiration(content.getExpiration()).build();
        }
        CorrelationData data = new CorrelationData(content.getId());
        //存储到redis
        redisTemplate.opsForValue().set(data.getId(), JSONObject.toJSONString(content));
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message, data);
    }
}