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
public class RedisOnlineUsersPoolConfig {

    @Value("${spring.redis.onlineUsers}")
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
     *
     * @author MrLu
     * @createTime 2020/9/29 10:05
     */
    @Bean
    @Qualifier("createOnlineUserLettuceConnectionFactory")
    LettuceConnectionFactory createOnlineUserLettuceConnectionFactory(GenericObjectPoolConfig genericObjectPoolConfig) {

        //在RedisSessionPoolConfig 注入
        genericObjectPoolConfig.setMaxIdle(maxIdle);
        genericObjectPoolConfig.setMinIdle(minIdle);
        genericObjectPoolConfig.setMaxTotal(maxActive);
        genericObjectPoolConfig.setMaxWaitMillis(maxWait);
        genericObjectPoolConfig.setTimeBetweenEvictionRunsMillis(100);
//        builder.poolConfig(genericObjectPoolConfig);
        LettuceClientConfiguration lettuceClientConfiguration = LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(timeout))
                .shutdownTimeout(Duration.ofMillis(shutdownTimeOut))
                .poolConfig(genericObjectPoolConfig)
                .build();
        //redis配置
        RedisStandaloneConfiguration redisConfiguration = new
                RedisStandaloneConfiguration(host, port);
        redisConfiguration.setDatabase(sessionDatabaseIndex);
        redisConfiguration.setPassword(password);


        //根据配置和客户端配置创建连接
        return new
                LettuceConnectionFactory(redisConfiguration, lettuceClientConfiguration);
    }
}
