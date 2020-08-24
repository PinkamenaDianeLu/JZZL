package com.database.sqlsession;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author MrLu
 * @createTime 2020/8/24 11:36
 * @describe 安综sqlSession工厂
 */
public class ZfbaSqlSessionFactory {
    private  static SqlSessionFactory sqlSessionFactory=null;
    //类线程锁
    private static final Class CLASS_LOCK= ZfbaSqlSessionFactory.class;
    private ZfbaSqlSessionFactory(){
    }

    /**
     * 创建sqlsession工程
     * @author MrLu
     * @createTime  2020/8/21 15:14
     * @return  SqlSessionFactory  |
     */
    public  static SqlSessionFactory initSqlSessionFactory(){
        String resource= "mybatis-config.xml";
        InputStream inputStream=null;
        try {
            inputStream= Resources.getResourceAsStream(resource);

        }catch (IOException ex){
            ex.printStackTrace();
        }
        synchronized (CLASS_LOCK){
            if (sqlSessionFactory ==null){
                sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream,"zfba");
            }
        }
        return  sqlSessionFactory;
    }
    /**
     * 打开sqlsession
     * @author MrLu
     * @createTime  2020/8/21 15:14
     * @return  SqlSession  |
     */
    public static SqlSession openSqlSession(){
        if (sqlSessionFactory==null){
            initSqlSessionFactory();
        }
        return  sqlSessionFactory.openSession();
    }
}
