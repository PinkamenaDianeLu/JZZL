package com.database;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.ibatis.datasource.DataSourceFactory;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author MrLu
 * @createTime 2020/8/21 16:22
 * @describe 定义使用连接池Druid
 */
public class DruidDatabase extends DruidDataSourceFactory implements DataSourceFactory {
    private Properties properties = null;

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public DataSource getDataSource() {
        DataSource dataSource = null;
        try {
            dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dataSource;
    }
}
