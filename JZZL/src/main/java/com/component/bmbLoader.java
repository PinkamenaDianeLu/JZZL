package com.component;

import com.bean.jzgl.DTO.SysBmbDTO;
import com.module.SystemManagement.Services.BmbService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MrLu
 * @createTime 2020/9/27 9:49
 * @describe 将编码表上传至redis
 */
@Component
public class bmbLoader implements CommandLineRunner {

    final
    RedisTemplate<String, Object> redisCCTemplate;
    final
    BmbService bmbService;

    public bmbLoader(BmbService bmbService, RedisTemplate<String, Object> redisCCTemplate) {
        this.bmbService = bmbService;
        this.redisCCTemplate = redisCCTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        //加载bmb到redis
        go();
    }

   // @Scheduled(cron = "0 0 10 * * ?")
    public  void  gogo(){
        go();
    }
    private  void go(){
        List<String> tyList= bmbService.getBmbType();
        for (String thisType:
                tyList) {
            //查询所有编码
            List<SysBmbDTO> bmbList=bmbService.getBmbMapByType(thisType);
            Map<String,String> redisCacheMap=new HashMap<>();
            //将编码转换成map
            for (SysBmbDTO thisSysBmbDTO:
            bmbList) {
                redisCacheMap.put(thisSysBmbDTO.getCode(),thisSysBmbDTO.getCodename());
            }
            //缓存至redis
            redisCCTemplate.opsForValue().set(thisType,redisCacheMap);
        }
    }
}
