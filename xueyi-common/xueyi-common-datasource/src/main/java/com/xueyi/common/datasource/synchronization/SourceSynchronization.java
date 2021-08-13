package com.xueyi.common.datasource.synchronization;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.xueyi.common.core.constant.TenantConstants;
import com.xueyi.common.datasource.utils.DSUtils;
import com.xueyi.common.core.constant.MessageConstant;
import com.xueyi.common.message.domain.Message;
import com.xueyi.common.message.listener.BaseListener;
import com.xueyi.tenant.api.domain.source.TenantSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * 源同步
 *
 * @author Ethereal
 */
@Configuration
public class SourceSynchronization extends BaseListener {

    private static final Logger logger = LoggerFactory.getLogger(SourceSynchronization.class);

    @Value("${spring.rabbitmq.datasource.synchro.queue.name}")
    private String dataSourceSynchroQueueName;

    @Value("${spring.rabbitmq.datasource.synchro.exchange.name}")
    private String dataSourceSynchroExchangeName;

    @Bean
    public DirectExchange dataSourceDirectExchange() {
        return new DirectExchange(dataSourceSynchroExchangeName);
    }

    @Bean
    public Queue dataSourceQueue() {
        return new Queue(dataSourceSynchroQueueName);
    }

    /**
     * 绑定
     */
    @Bean
    public Binding bindingTest() {
        return BindingBuilder.bind(dataSourceQueue()).to(dataSourceDirectExchange()).with(MessageConstant.ROUTING_KEY_SOURCE);
    }

    @Override
    protected void execute(Message message) {
        if (message.getData() != null) {
            TenantSource source = JSONObject.toJavaObject(JSON.parseObject(message.getData().toString()), TenantSource.class);
            if (source.getSyncType() == TenantConstants.SYNC_TYPE_REFRESH) {
                DSUtils.delDs(source.getSlave());
                DSUtils.addDs(source);
            } else if (source.getSyncType() == TenantConstants.SYNC_TYPE_ADD) {
                DSUtils.addDs(source);
            } else if (source.getSyncType() == TenantConstants.SYNC_TYPE_DELETE) {
                DSUtils.delDs(source.getSlave());
            }
        }
    }

    @Override
    protected void failExecute(Message message) {
        logger.info("失败处理：{}", message.toString());
    }

    @RabbitListener(queues = {"${spring.rabbitmq.datasource.synchro.queue.name}"})
    @Override
    protected void receiveMessage(org.springframework.amqp.core.Message message, Channel channel) throws IOException {
        super.receiveMessage(message, channel);
    }
}