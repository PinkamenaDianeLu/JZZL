package com.dataSource;

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
 * @createTime 2020/8/14
 * @describe 卷宗整理数据源
 */
@Configuration
@MapperScan(value = "com.mapper.jzgl", sqlSessionFactoryRef = "JzglSqlSessionBean")
public class JzglDataSource {
    @Autowired
    @Qualifier("jzgl")
    DataSource jzgl;
    @Bean
    SqlSessionFactory JzglSqlSessionBean() throws  Exception{
        SqlSessionFactoryBean sqlSessionFactoryBean=new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(jzgl);
        return sqlSessionFactoryBean.getObject();
    }
    @Bean
    SqlSessionTemplate JzzlSqlSessionTemplate()throws Exception{
        return  new SqlSessionTemplate(JzglSqlSessionBean());
    }

}
