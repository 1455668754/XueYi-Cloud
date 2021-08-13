package com.xueyi.common.datasource.utils;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.xueyi.common.core.constant.MessageConstant;
import com.xueyi.common.core.utils.IdUtils;
import com.xueyi.common.core.utils.SpringUtils;
import com.xueyi.common.core.utils.bean.BeanUtils;
import com.xueyi.common.message.domain.Message;
import com.xueyi.common.message.service.ProducerService;
import com.xueyi.tenant.api.domain.source.TenantSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;

/**
 * 源管理工具类
 *
 * @author xueyi
 */
public class DSUtils {

    /**
     * 添加一个数据源到数据源库中
     *
     * @param tenantSource 数据源对象
     * @return 结果
     */
    public static boolean addDs(TenantSource tenantSource) {
        try {
            DefaultDataSourceCreator dataSourceCreator = SpringUtils.getBean(DefaultDataSourceCreator.class);
            DataSourceProperty dataSourceProperty = new DataSourceProperty();
            BeanUtils.copyProperties(tenantSource, dataSourceProperty);
            DataSource dataSource = SpringUtils.getBean(DataSource.class);
            DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
            dataSource = dataSourceCreator.createDataSource(dataSourceProperty);
            ds.addDataSource(tenantSource.getSlave(), dataSource);
            return true;
        } catch (BeansException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从数据源库中删除一个数据源
     *
     * @param slave 数据源编码
     * @return 结果
     */
    public static boolean delDs(String slave) {
        try {
            DynamicRoutingDataSource ds = (DynamicRoutingDataSource) SpringUtils.getBean(DataSource.class);
            ds.removeDataSource(slave);
            return true;
        } catch (BeansException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取当前数据源库中所有数据源
     */
    public static void getDs() {
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) SpringUtils.getBean(DataSource.class);
        ds.getCurrentDataSources().keySet().forEach(System.out::println);
    }

    /**
     * 异步同步数据源到数据源库
     *
     * @param tenantSource 数据源对象
     */
    public static void syncDS(TenantSource tenantSource)
    {
        ProducerService producerService = SpringUtils.getBean(ProducerService.class);
        Message message = new Message(IdUtils.randomUUID(), tenantSource);
        producerService.sendMsg(message, MessageConstant.EXCHANGE_SOURCE, MessageConstant.ROUTING_KEY_SOURCE);
    }
}