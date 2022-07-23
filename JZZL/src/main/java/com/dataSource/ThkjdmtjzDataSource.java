package com.dataSource;/**
 * @author Mrlu
 * @createTime 2021/6/17
 * @describe
 */

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author MrLu
 * @createTime 2021/6/17 10:04
 * @describe 多媒体卷宗数据源
 */
@Configuration
@MapperScan(value = "com.mapper.thkjdmtjz", sqlSessionFactoryRef = "ThkjdmtjzSqlSessionBean")
public class ThkjdmtjzDataSource {

    @Autowired
    @Qualifier("thkjdmtjz")
    DataSource thkjdmtjz;
    @Bean
    SqlSessionFactory ThkjdmtjzSqlSessionBean() throws  Exception{
        SqlSessionFactoryBean sqlSessionFactoryBean=new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(thkjdmtjz);
        return sqlSessionFactoryBean.getObject();
    }
    @Bean
    SqlSessionTemplate thkjdmtjzSqlSessionTemplate()throws Exception{
        return  new SqlSessionTemplate(ThkjdmtjzSqlSessionBean());
    }
}
