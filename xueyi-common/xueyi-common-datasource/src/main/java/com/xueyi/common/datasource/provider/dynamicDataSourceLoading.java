package com.xueyi.common.datasource.provider;

import com.baomidou.dynamic.datasource.provider.AbstractJdbcDataSourceProvider;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * 源加载
 *
 * @author xueyi
 */
@Configuration
public class dynamicDataSourceLoading {

    @Value("${spring.datasource.dynamic.datasource.master.driver-class-name}")
    private String Driver;
    @Value("${spring.datasource.dynamic.datasource.master.url}")
    private String Url;
    @Value("${spring.datasource.dynamic.datasource.master.username}")
    private String UserName;
    @Value("${spring.datasource.dynamic.datasource.master.password}")
    private String PassWord;

    @Bean
    public DynamicDataSourceProvider jdbcDynamicDataSourceProvider() {
        return new AbstractJdbcDataSourceProvider(Driver, Url, UserName, PassWord) {
            @Override
            protected Map<String, DataSourceProperty> executeStmt(Statement statement) throws SQLException {
                ResultSet rs = statement.executeQuery("select s.slave,s.username,s.password,s.url,s.driver_class_name from xy_tenant_source s where s.status = 0 and s.del_flag = 0");
                Map<String, DataSourceProperty> map = new HashMap<String, DataSourceProperty>();
                while (rs.next()) {
                    String name = rs.getString("slave");
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    String url = rs.getString("url");
                    String driver = rs.getString("driver_class_name");
                    DataSourceProperty property = new DataSourceProperty();
                    property.setUsername(username);
                    property.setPassword(password);
                    property.setUrl(url);
                    property.setDriverClassName(driver);
                    map.put(name, property);
                }
                return map;
            }
        };
    }
}