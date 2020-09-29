package com.config.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;

import java.time.Duration;

/**
 * @author MrLu
 * @createTime 2020/9/29 10:02
 * @describe
 */
@Configuration
@PropertySource("classpath:application.yml")
public class RedisCachePoolConfig {

    @Value("${spring.redis.cacheDatabase}")
    private Integer sessionDatabaseIndex;

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.timeout}")
    private long timeout;

    @Value("${spring.redis.lettuce.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.lettuce.pool.min-idle}")
    private int minIdle;

    @Value("${spring.redis.lettuce.pool.max-active}")
    private int maxActive;

    @Value("${spring.redis.lettuce.pool.max-wait}")
    private long maxWait;
    @Value("${spring.redis.timeout}")
    private Long timeOut;

    @Value("${spring.redis.lettuce.shutdown-timeout}")
    private Long shutdownTimeOut;
     /**
     * cache内容缓存redis连接工厂
     * @author MrLu
     * @createTime  2020/9/29 10:05
      */
    @Bean
    @Qualifier("createCacheLettuceConnectionFactory")
    LettuceConnectionFactory createCacheLettuceConnectionFactory(GenericObjectPoolConfig genericObjectPoolConfig){

        //redis配置
        RedisConfiguration redisConfiguration = new
                RedisStandaloneConfiguration(host,port);
        ((RedisStandaloneConfiguration) redisConfiguration).setDatabase(sessionDatabaseIndex);
        ((RedisStandaloneConfiguration) redisConfiguration).setPassword(password);

        //redis客户端配置
        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder
                builder =  LettucePoolingClientConfiguration.builder().
                commandTimeout(Duration.ofMillis(timeout));

        builder.shutdownTimeout(Duration.ofMillis(shutdownTimeOut));
        //在RedisSessionPoolConfig 注入
        builder.poolConfig(genericObjectPoolConfig);
        LettuceClientConfiguration lettuceClientConfiguration = builder.build();

        //根据配置和客户端配置创建连接
        LettuceConnectionFactory lettuceConnectionFactory = new
                LettuceConnectionFactory(redisConfiguration,lettuceClientConfiguration);
        return lettuceConnectionFactory;
    }
}
