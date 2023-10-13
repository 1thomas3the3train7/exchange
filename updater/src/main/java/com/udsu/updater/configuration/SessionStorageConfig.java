package com.udsu.updater.configuration;

import org.jctools.maps.NonBlockingHashMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.socket.WebSocketSession;

@Configuration
public class SessionStorageConfig {
    @Bean
    public NonBlockingHashMap<String, WebSocketSession> sessionStorage() {
        return new NonBlockingHashMap<>();
    }
}
