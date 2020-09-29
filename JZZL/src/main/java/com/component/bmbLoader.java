package com.component;

import com.module.SystemManagement.Services.BmbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author MrLu
 * @createTime 2020/9/27 9:49
 * @describe
 */
@Component
public class bmbLoader implements CommandLineRunner {

    @Autowired
    RedisTemplate<String, Object> redisCCTemplate;
    @Autowired
    BmbService bmbService;
    @Override
    public void run(String... args) throws Exception {
        //加载bmb到redis
        go();
    }

    @Scheduled(cron = "0 0 10 * * ?")
    public  void  gogo(){
        go();
    }
    private  void go(){
        List<String> tyList= bmbService.getBmbType();
        for (String thisType:
                tyList) {
            redisCCTemplate.opsForValue().set(thisType, bmbService.getBmbMapByType(thisType));
        }
    }
}
