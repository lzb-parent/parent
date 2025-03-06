package com.pro.common.web.security;

import com.pro.common.web.security.websocket.MyPrincipalHandshakeHandler;
import com.pro.common.web.security.websocket.MyWebSocketHandlerDecoratorFactory;
import com.pro.common.modules.service.dependencies.modelauth.base.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

@Slf4j
@Configuration
@DependsOn("jTDServiceImpl")
@EnableWebSocketMessageBroker // 注解开启STOMP协议来传输基于代理的消息，此时控制器支持使用@MessageMapping
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    public static final String WebSocket_EndPoint = "/websocket/server";
    @Autowired
    private TokenService tokenService;

    /**
     * 注册stomp端点，主要是起到连接作用
     *
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(WebSocket_EndPoint)
                .setAllowedOriginPatterns("*") // 或者 .setAllowedOrigins("http://example.com")
//                .addInterceptors(new HandleShakeInterceptors())
                .setHandshakeHandler(new MyPrincipalHandshakeHandler(tokenService))
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 用户订阅主题的前缀， /topic：广播， /queue 点对点
//        registry.enableSimpleBroker("/topic", "/queue");
        registry.enableSimpleBroker("");
        // 客户端发送的消息，要以/app为前缀，再经过Broker转发给相应的controller
        registry.setApplicationDestinationPrefixes("/app");
        // 指定用户发送一对一的主题前缀 /user
        registry.setUserDestinationPrefix("/user");
    }

    @Bean
    @ConditionalOnMissingBean
    public WebSocketHandlerDecoratorFactory webSocketFactory() {
        return new MyWebSocketHandlerDecoratorFactory();
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.addDecoratorFactory(webSocketFactory());
    }
}
