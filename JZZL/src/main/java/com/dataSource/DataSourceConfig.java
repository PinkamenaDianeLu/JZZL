package com.dataSource;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

 /**
 *  用于配置多数据源
 * @author MrLu
 * @createTime  2020/8/14 9:21
  */
@Configuration
public class DataSourceConfig {

    //TODO MrLu 2020/8/21  druid配置url统计  
     /**
     * 卷宗整理 数据源
     * @author MrLu
     * @createTime  2020/8/14 9:22
      */
    @Bean
    @ConfigurationProperties("spring.datasource.jzgl")
    DataSource jzgl(){
        return DruidDataSourceBuilder.create().build();
   }

    /**
    * 执法办案 数据源
    * @author MrLu
    * @createTime  2020/8/14 9:22
     */
     @Bean
     @ConfigurationProperties("spring.datasource.zfba")
     DataSource zfba(){
         return DruidDataSourceBuilder.create().build();
     }
}
