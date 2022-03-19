package com.xueyi.common.datasource.config;

import com.baomidou.dynamic.datasource.processor.DsHeaderProcessor;
import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.baomidou.dynamic.datasource.processor.DsSessionProcessor;
import com.baomidou.dynamic.datasource.processor.DsSpelExpressionProcessor;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration;
import com.xueyi.common.datasource.processor.DsIsolateExpressionProcessor;
import com.xueyi.common.datasource.processor.DsMasterExpressionProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 源访问策略注入
 *
 * @author xueyi
 */
@Configuration
@AutoConfigureBefore(DynamicDataSourceAutoConfiguration.class)
public class MyDynamicDataSourceConfig {

    @Bean
    public DsProcessor dsProcessor() {
        DsHeaderProcessor headerProcessor = new DsHeaderProcessor();
        DsSessionProcessor sessionProcessor = new DsSessionProcessor();
        DsSpelExpressionProcessor spelExpressionProcessor = new DsSpelExpressionProcessor();
        DsIsolateExpressionProcessor isolateExpressionProcessor = new DsIsolateExpressionProcessor();
        DsMasterExpressionProcessor masterExpressionProcessor = new DsMasterExpressionProcessor();
        headerProcessor.setNextProcessor(sessionProcessor);
        sessionProcessor.setNextProcessor(spelExpressionProcessor);
        spelExpressionProcessor.setNextProcessor(isolateExpressionProcessor);
        isolateExpressionProcessor.setNextProcessor(masterExpressionProcessor);
        return headerProcessor;
    }

    /**
     * 解决warn- discard long time none received connection xxx
     * druid会优先使用pingMethod方法来检查空闲连接
     * 通过设置druid.mysql.usePingMethod=false，让其使用validationQuery语句
     */
    @PostConstruct
    public void setProperty() {
        System.setProperty("druid.mysql.usePingMethod", "false");
    }
}