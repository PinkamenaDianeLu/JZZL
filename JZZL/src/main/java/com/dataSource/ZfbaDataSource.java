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
 * @author Mrlu
 * @createTime 2020/8/14
 * @describe 执法办案数据源
 */


@Configuration
@MapperScan(value = "com.mapper.zfba", sqlSessionFactoryRef = "ZfbaSqlSessionBean")
public class ZfbaDataSource {
    @Autowired
    @Qualifier("zfba")
    DataSource zfba;
    @Bean
    SqlSessionFactory ZfbaSqlSessionBean() throws  Exception{
        SqlSessionFactoryBean sqlSessionFactoryBean=new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(zfba);
        return sqlSessionFactoryBean.getObject();
    }
    @Bean
    SqlSessionTemplate ZfbaSqlSessionTemplate()throws Exception{
        return  new SqlSessionTemplate(ZfbaSqlSessionBean());
    }
}
