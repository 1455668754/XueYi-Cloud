package com.xueyi.common.datasource.utils;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.xueyi.common.core.utils.SpringUtils;
import com.xueyi.common.core.utils.bean.BeanUtils;
import com.xueyi.tenant.api.source.TenantSource;
import org.springframework.beans.BeansException;

import javax.sql.DataSource;

public class DSUtils {
    /**
     * 添加一个数据源到数据源库中
     *
     * @param tenantSource 数据源对象
     * @return 结果
     */
    public static int addDs(TenantSource tenantSource) {
        try {
            DefaultDataSourceCreator dataSourceCreator = SpringUtils.getBean(DefaultDataSourceCreator.class);
            DataSourceProperty dataSourceProperty = new DataSourceProperty();
            BeanUtils.copyProperties(tenantSource, dataSourceProperty);
            DataSource dataSource = SpringUtils.getBean(DataSource.class);
            DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
            dataSource = dataSourceCreator.createDataSource(dataSourceProperty);
            ds.addDataSource(tenantSource.getSlave(), dataSource);
            return 1;
        } catch (BeansException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 从数据源库中删除一个数据源
     *
     * @param slave 数据源编码
     * @return 结果
     */
    public static int delDs(String slave) {
        try {
            DynamicRoutingDataSource ds = (DynamicRoutingDataSource) SpringUtils.getBean(DataSource.class);
            ds.removeDataSource(slave);
            return 1;
        } catch (BeansException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获取当前数据源库中所有数据源
     */
    public static void getCurrentAllDataSources() {
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) SpringUtils.getBean(DataSource.class);
        ds.getCurrentDataSources().keySet().forEach(s ->
        {
            System.out.println(s);
        });
    }
}
