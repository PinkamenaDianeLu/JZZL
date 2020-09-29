package com.component;

import com.util.GlobalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author MrLu
 * @createTime 2020/9/27 9:48
 * @describe 加载盐 预计用于更新并加载到redis中
 */
@Component
public class saltLoader implements CommandLineRunner {
    @Autowired
    RedisTemplate<String, Object> redisCCTemplate;
    @Override
    public void run(String... args) throws Exception {
       go();
    }
    @Scheduled(cron = "0 0 1 * * ?")
    public  void  gogo(){
        go();
    }


    private  void go(){
        LocalDateTime currentTime = LocalDateTime.now();
        String saltSalt = currentTime.getHour() + "" + currentTime.getSecond();
        String finSalt = GlobalUtil.getGlobal("salt") + saltSalt;
        redisCCTemplate.opsForValue().set("salt", finSalt);
    }
}
