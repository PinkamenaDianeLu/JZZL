package com.config.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
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
 * @author Mrlu
 * @createTime 2020/5/5
 * @describe
 */
@Configuration
@PropertySource("classpath:application.yml")
public class RdisPoolConfig {

    @Value("${spring.redis.sessionDatabase}")
    private Integer sessionDatabaseIndex;
    //如果想把码表也缓存的话启用这个池  但是没有写定时更新相关的代码
//    @Value("${spring.redis.database}")
//    private int database;

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
    @Bean
      LettuceConnectionFactory createLettuceConnectionFactory(GenericObjectPoolConfig genericObjectPoolConfig){

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
        builder.poolConfig(genericObjectPoolConfig);
        LettuceClientConfiguration lettuceClientConfiguration = builder.build();

        //根据配置和客户端配置创建连接
        LettuceConnectionFactory lettuceConnectionFactory = new
                LettuceConnectionFactory(redisConfiguration,lettuceClientConfiguration);
//        lettuceConnectionFactory .afterPropertiesSet();

        return lettuceConnectionFactory;
    }
     /**
     * @author Mrlu
     * @param
     * @createTime  2020/5/5 10:44
     * @describe 连接池配置
     * @version 1.0
      */
    @Bean
    public GenericObjectPoolConfig genericObjectPoolConfig() {
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxIdle(maxIdle);
        genericObjectPoolConfig.setMinIdle(minIdle);
        genericObjectPoolConfig.setMaxTotal(maxActive);
        genericObjectPoolConfig.setMaxWaitMillis(maxWait);
        return genericObjectPoolConfig;
    }
}
