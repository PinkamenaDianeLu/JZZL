package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ServletComponentScan
@SpringBootApplication
@EnableTransactionManagement
@EnableAspectJAutoProxy
public class JzzlApplication {

     /**
     * 启动方法
     * @author MrLu
     * @createTime  2020/8/13 17:26
      */
    public static void main(String[] args) {
        SpringApplication.run(JzzlApplication.class, args);
    }
}
