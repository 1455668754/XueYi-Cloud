package com.xueyi.common.message.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Rabbit配置类
 * direct直连模型
 * fanout无路由模式，使用场景广播消息
 * topic 模糊路由模式，适用业务分组
 * fanout > direct > topic 这里是多消费模式，topic和fanout都能实现，通过性能对比选择fanout  11 > 10 > 6
 *
 * @author Ethereal
 */
@Configuration
public class MessageConfig {

    /**
     * 初始化相关配置
     */
    @Bean
    public MessageTemplateConfig rabbitTemplateConfig() {
        return new MessageTemplateConfig();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }
}