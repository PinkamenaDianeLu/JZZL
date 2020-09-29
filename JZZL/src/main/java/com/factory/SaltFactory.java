package com.factory;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import sun.misc.BASE64Decoder;

import java.util.Map;

/**
 * @author MrLu
 * @createTime 2020/9/29 10:40
 * @describe
 */
public class SaltFactory {
    @Autowired
    RedisTemplate<String, Object> redisCCTemplate;
    public  String DecodeUrlP(String p) throws Exception{
        String finSalt = (String) redisCCTemplate.opsForValue().get("salt");
        if (StringUtils.isEmpty(finSalt)){
            throw  new  Exception("salt获取异常！请重启项目并检查redis连接");
        }else {
            final BASE64Decoder decoder = new BASE64Decoder();

            return  new String(decoder.decodeBuffer(p), "UTF-8").replace(finSalt,"");
        }
    }

    public  Map<String,String>  bmb(String type) throws Exception{
        Map<String,String> finSalt = (Map<String, String>) redisCCTemplate.opsForValue().get(type);
        assert finSalt != null;
        if (finSalt.size()==0){
            throw  new  Exception("salt获取异常！请重启项目并检查redis连接");
        }else {
            return  finSalt;
        }
    }
}
