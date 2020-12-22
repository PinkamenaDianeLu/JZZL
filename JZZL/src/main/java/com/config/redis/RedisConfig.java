package com.config.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author MrLu
 * @createTime 2020/4/28
 * @describe redis 模板配置
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisConfig  extends CachingConfigurerSupport {
     /**
     * @author MrLu
     * @createTime  2020/4/28 23:34
     * @describe  将定义新的模板使用Serializable缓存数据在redis上 缓存位置： session
     * @version 1.0
      */
    @Bean
    public RedisTemplate<String, Serializable> redisCSTemplate(@Qualifier("createSessionLettuceConnectionFactory") @Autowired LettuceConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Serializable> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

     /**
     * @author MrLu
     * @createTime  2020/4/28 23:55
     * @describe 将定义的List缓存在redis上 缓存位置： session
     * @version 1.0
      */
    @Bean
    public RedisTemplate<String, Object> redisCLTemplate(@Qualifier("createSessionLettuceConnectionFactory") @Autowired RedisConnectionFactory cf) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        initDomainRedisTemplate(redisTemplate,cf);
        return redisTemplate;
    }


     /**
     * 将定义的Map缓存在redis上 缓存位置： cache
     * @author MrLu
     * @createTime  2020/9/29 10:24
     * @return  RedisTemplate<String, Object>  |
      */
    @Bean
    public RedisTemplate<String, Object> redisCCTemplate(@Qualifier("createCacheLettuceConnectionFactory") @Autowired RedisConnectionFactory cf) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        initDomainRedisTemplate(redisTemplate,cf);
        return redisTemplate;
    }

     /**
     * 将用户记录再在线用户上 缓存位置： onlineUsers
     * @author MrLu
     * @createTime  2020/12/18 9:29
      * @return  RedisTemplate<String, Object>  | <用户username,登录时间>
      */
    @Bean
    public RedisTemplate<String, Object> redisOnlineUserTemplate(@Qualifier("createOnlineUserLettuceConnectionFactory") @Autowired RedisConnectionFactory cf) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        initDomainRedisTemplate(redisTemplate,cf);
        return redisTemplate;
    }


    private void initDomainRedisTemplate(RedisTemplate<String, Object> redisTemplate, RedisConnectionFactory cf) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(jackson2JsonRedisSerializer());
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer());
        redisTemplate.setConnectionFactory(cf);
        redisTemplate.afterPropertiesSet();
    }



    @Bean
    public Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer() {
        final Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(
                Object.class);
        final ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();
        objectMapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        return jackson2JsonRedisSerializer;
    }


    @Bean
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {

            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };
    }


    @CacheEvict(allEntries = true, cacheNames = { "manufacturedGood", "rawMaterial"})
    @Scheduled(fixedDelay = 60*1000)
    public void cacheEvict() {
    }

}
