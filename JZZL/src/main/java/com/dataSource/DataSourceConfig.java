package com.dataSource;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

 /**
 *  用于配置多数据源
 * @author Mrlu
 * @createTime  2020/8/14 9:21
  */
@Configuration
public class DataSourceConfig {

     /**
     * 卷宗整理 数据源
     * @author Mrlu
     * @createTime  2020/8/14 9:22
      */
    @Bean
    @ConfigurationProperties("spring.datasource.jzgl")
    DataSource jzgl(){
        return DruidDataSourceBuilder.create().build();
   }

    /**
    * 执法办案 数据源
    * @author Mrlu
    * @createTime  2020/8/14 9:22
     */
     @Bean
     @ConfigurationProperties("spring.datasource.zfba")
     DataSource zfba(){
         return DruidDataSourceBuilder.create().build();
     }
}
