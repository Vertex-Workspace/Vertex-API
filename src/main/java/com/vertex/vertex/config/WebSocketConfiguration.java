package com.vertex.vertex.config;

import com.vertex.vertex.config.handler.UsedWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@EnableWebSocket
@CrossOrigin
@Configuration
public class WebSocketConfiguration implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(getWebSocketHandler(),"/chat", "/notifications", "/tasks").setAllowedOrigins("*");
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();

        container.setMaxTextMessageBufferSize(99999999);

        return container;
    }

    @Bean
    public WebSocketHandler getWebSocketHandler(){
        return new UsedWebSocketHandler();
    }
}
