package com.config.webSocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @author Mrlu
 * @createTime 2020/5/22
 * @describe webSocket
 */
@Configuration
@EnableWebSocketMessageBroker
public class webSocketConfig implements WebSocketMessageBrokerConfigurer {
    public static final String TOPIC = "/topic/greetings";

    public static final String ENDPOINT = "/chat";

    public static final String APP_PREFIX = "/app";

    public static final String HELLO_MAPPING = "/hello";


     /**
     * @author Mrlu
     * @param
     * @createTime  2020/5/23 0:03
     * @describe 消息代理前缀
     * @version 1.0
      */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic","/queue");
        config.setApplicationDestinationPrefixes("/app");//过滤出需要被注解方法处理的消息
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat").setAllowedOrigins("*").withSockJS();//解决浏览器兼容性问题
    }


}
