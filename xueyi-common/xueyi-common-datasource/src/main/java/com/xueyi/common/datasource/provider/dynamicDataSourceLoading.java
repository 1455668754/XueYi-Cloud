package com.xueyi.common.datasource.provider;

import com.baomidou.dynamic.datasource.provider.AbstractJdbcDataSourceProvider;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.xueyi.common.core.constant.basic.SourceConstants;
import com.xueyi.common.datasource.config.properties.SourceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import static com.xueyi.common.core.constant.basic.SourceConstants.Details;

/**
 * 子数据源加载
 *
 * @author xueyi
 */
@Configuration
public class DynamicDataSourceLoading {

    @Autowired
    SourceProperties sourceProperties;

    @Bean
    public DynamicDataSourceProvider jdbcDynamicDataSourceProvider() {
        return new AbstractJdbcDataSourceProvider(sourceProperties.getDriverClassName(), sourceProperties.getUrl(), sourceProperties.getUsername(), sourceProperties.getPassword()) {
            @Override
            protected Map<String, DataSourceProperty> executeStmt(Statement statement) throws SQLException {
                ResultSet rs = statement.executeQuery(SourceConstants.SELECT_SOURCE);
                Map<String, DataSourceProperty> map = new HashMap<>();
                while (rs.next()) {
                    String name = rs.getString(Details.SLAVE.getCode());
                    String username = rs.getString(Details.USERNAME.getCode());
                    String password = rs.getString(Details.PASSWORD.getCode());
                    String url = rs.getString(Details.URL_PREPEND.getCode()).concat(rs.getString(Details.URL_APPEND.getCode()));
                    String driver = rs.getString(Details.DRIVER_CLASS_NAME.getCode());
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