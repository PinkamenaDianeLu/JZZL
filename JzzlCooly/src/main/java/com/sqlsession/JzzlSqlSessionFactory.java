package com.sqlsession;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author MrLu
 * @createTime 2020/8/21 14:48
 * @describe
 */
public class JzzlSqlSessionFactory {
    private  static SqlSessionFactory sqlSessionFactory=null;
    //类线程锁
    private static final Class CLASS_LOCK= JzzlSqlSessionFactory.class;
    private JzzlSqlSessionFactory(){
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
                sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream,"jzzl");
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
