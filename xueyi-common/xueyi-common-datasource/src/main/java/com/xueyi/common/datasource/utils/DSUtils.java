package com.xueyi.common.datasource.utils;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.xueyi.common.core.constant.MessageConstant;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.utils.IdUtils;
import com.xueyi.common.core.utils.SpringUtils;
import com.xueyi.common.core.utils.bean.BeanUtils;
import com.xueyi.common.message.domain.Message;
import com.xueyi.common.message.service.ProducerService;
import com.xueyi.tenant.api.domain.source.Source;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 源管理工具类
 *
 * @author xueyi
 */
public class DSUtils {

    /**
     * 添加一个数据源到数据源库中
     *
     * @param source 数据源对象
     */
    public static void addDs(Source source) {
        try {
            DefaultDataSourceCreator dataSourceCreator = SpringUtils.getBean(DefaultDataSourceCreator.class);
            DataSourceProperty dataSourceProperty = new DataSourceProperty();
            BeanUtils.copyProperties(source, dataSourceProperty);
            DataSource dataSource = SpringUtils.getBean(DataSource.class);
            DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
            dataSource = dataSourceCreator.createDataSource(dataSourceProperty);
            ds.addDataSource(source.getSlave(), dataSource);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("数据源添加失败");
        }
    }

    /**
     * 从数据源库中删除一个数据源
     *
     * @param slave 数据源编码
     */
    public static void delDs(String slave) {
        try {
            DynamicRoutingDataSource ds = (DynamicRoutingDataSource) SpringUtils.getBean(DataSource.class);
            ds.removeDataSource(slave);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("数据源删除失败");
        }
    }

    /**
     * 获取当前数据源库中所有数据源
     */
    public static void getDs() {
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) SpringUtils.getBean(DataSource.class);
        ds.getDataSources().keySet().forEach(System.out::println);
    }

    /**
     * 获取当前线程数据源名称
     */
    public static String getNowDsName() {
        return DynamicDataSourceContextHolder.peek();
    }

    /**
     * 异步同步数据源到数据源库
     *
     * @param source 数据源对象
     */
    public static void syncDs(Source source) {
        ProducerService producerService = SpringUtils.getBean(ProducerService.class);
        Message message = new Message(IdUtils.randomUUID(), source);
        producerService.sendMsg(message, MessageConstant.EXCHANGE_SOURCE, MessageConstant.ROUTING_KEY_SOURCE);
    }

    /**
     * 测试数据源是否可连接
     *
     * @param source 数据源对象
     */
    public static void testDs(Source source) {
        try {
            Class.forName(source.getDriverClassName());
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            throw new ServiceException("数据源驱动加载失败");
        }
        try {
            Connection dbConn= DriverManager.getConnection(source.getUrlPrepend()+source.getUrlAppend(),source.getUsername(),source.getPassword());
            dbConn.close();
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            throw new ServiceException("数据库连接失败");
        }
    }
}