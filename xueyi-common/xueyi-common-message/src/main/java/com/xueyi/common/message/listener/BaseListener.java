package com.xueyi.common.message.listener;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.xueyi.common.core.constant.MessageConstant;
import com.xueyi.common.message.domain.Message;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.SerializerMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * 消息队列-消费者 基类
 *
 * @author Ethereal
 */
public abstract class BaseListener {

    protected static Logger logger = LoggerFactory.getLogger(BaseListener.class);

    private static final SerializerMessageConverter SERIALIZER_MESSAGE_CONVERTER = new SerializerMessageConverter();

    private static final String ENCODING = Charset.defaultCharset().name();

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 业务执行方法
     *
     * @param content 消息体
     */
    protected abstract void execute(Message content) throws Exception;

    /**
     * 失败执行
     *
     * @param content 消息体
     */
    protected abstract void failExecute(Message content);

    /**
     * 接收到的消息处理
     *
     * @param message 消息
     * @param channel 信道
     */
    protected void receiveMessage(org.springframework.amqp.core.Message message, Channel channel) throws IOException {
        /* 防止重复消费，可以根据传过来的唯一Id先判断缓存数据库中是否有数据
          1、有数据则不消费，直接应答处理
          2、缓存没有数据，则进行消费处理数据，处理完后手动应答
          3、如果消息处理异常则，可以存入数据库中，手动处理（可以增加短信和邮件提醒功能）*/
        try {
            Message content = getContent(message);
            //已经消费，直接返回
            if (canConsume(content, message.getMessageProperties().getConsumerQueue())) {
                logger.info("[{}] 已经消费过", message.getMessageProperties().getConsumerQueue());
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                //消费当前消息
                execute(content);
                logger.info("[{}] 消费成功", message.getMessageProperties().getConsumerQueue());
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                //消费成功删除key
                //单个消息控制
                String redisCountKey = "retry" + message.getMessageProperties().getConsumerQueue() + content.getId();
                //队列控制
                String queueKey = "retry" + message.getMessageProperties().getConsumerQueue();
                redisTemplate.delete(content.getId());
                redisTemplate.delete(redisCountKey);
                redisTemplate.delete(queueKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (dealFailAck(message, channel)) {
                    logger.info("回归队列：{}", message);
                } else {
                    logger.error("消费失败：{}", message);
                    failExecute(getContent(message));
                }
            } catch (Exception e1) {
                //扔掉数据
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
                logger.error("重试消费失败：{}", message);
                failExecute(getContent(message));
            }
        }
    }

    /**
     * @param message 消息
     * @return 消息体
     */
    private Message getContent(org.springframework.amqp.core.Message message) {
        String body = getBodyContentAsString(message);
        return JSONObject.toJavaObject(JSONObject.parseObject(body), Message.class);
    }

    /**
     * 获取message的body
     *
     * @param message 消息
     * @return 结果
     */
    private String getBodyContentAsString(org.springframework.amqp.core.Message message) {
        if (message.getBody() == null) {
            return null;
        }
        try {
            String contentType = (message.getMessageProperties() != null) ? message.getMessageProperties().getContentType() : null;
            if (MessageProperties.CONTENT_TYPE_SERIALIZED_OBJECT.equals(contentType)) {
                return SERIALIZER_MESSAGE_CONVERTER.fromMessage(message).toString();
            }
            if (MessageProperties.CONTENT_TYPE_TEXT_PLAIN.equals(contentType) || MessageProperties.CONTENT_TYPE_JSON.equals(contentType) || MessageProperties.CONTENT_TYPE_JSON_ALT.equals(contentType) || MessageProperties.CONTENT_TYPE_XML.equals(contentType)) {
                return new String(message.getBody(), ENCODING);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return Arrays.toString(message.getBody()) + "(byte[" + message.getBody().length + "])";
    }

    /**
     * 失败ACK
     *
     * @param message 消息
     * @param channel 信道
     * @return 结果
     */
    private Boolean dealFailAck(org.springframework.amqp.core.Message message, Channel channel) throws IOException {
        Message content = getContent(message);
        //单个消息控制
        String redisCountKey = "retry" + message.getMessageProperties().getConsumerQueue() + content.getId();
        String retryCount = redisTemplate.opsForValue().get(redisCountKey);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        //队列控制
        String queueKey = "retry" + message.getMessageProperties().getConsumerQueue();
        //没有重试过一次
        if (StringUtils.isBlank(retryCount)) {
            if (Boolean.FALSE.equals(redisTemplate.opsForValue().setIfAbsent(queueKey, "lock", 25, TimeUnit.SECONDS))) {
                channel.basicNack(deliveryTag, false, true);
                logger.info("deliveryTag:" + deliveryTag);
                return true;
            }
            //预防队列太长，造成延迟时间过长
            redisTemplate.opsForValue().setIfAbsent(redisCountKey, "1", 5, TimeUnit.MINUTES);
            logger.info("开始第一次尝试：");
        } else {
            if (Integer.parseInt(retryCount) < MessageConstant.CONNECT_TIMES) {
                redisTemplate.opsForValue().set(redisCountKey, retryCount);
                logger.info("开始第 " + Integer.parseInt(retryCount) + " 次尝试：");
            } else {
                //扔掉消息，准备持久化
                redisTemplate.delete(redisCountKey);
                redisTemplate.delete(queueKey);
                channel.basicNack(deliveryTag, false, false);
                return false;
            }
        }
        channel.basicNack(deliveryTag, false, true);
        return true;
    }

    /**
     * 是否能消费，防止重复消费
     *
     * @param content   消息体
     * @param queueName 队列名称
     * @return 结果
     */
    private Boolean canConsume(Message content, String queueName) {
        if (redisTemplate.opsForValue().get(queueName + ":" + content.getId()) == null) {
            return false;
        } else {
            //存储消费标志
            redisTemplate.opsForValue().set(queueName + ":" + content.getId(), "lock", 30, TimeUnit.SECONDS);
            return true;
        }
    }
}