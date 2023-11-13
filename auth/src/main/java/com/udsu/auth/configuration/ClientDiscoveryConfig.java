package com.udsu.auth.configuration;

import com.discovery.service.client.service.DiscoveryService;
import com.discovery.service.client.service.impl.DiscoveryServiceImpl;
import com.discovery.service.client.service.rsocket.RSocketClientRequester;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Configuration
public class ClientDiscoveryConfig {
    private final String DISCOVERY_URL = "http://localhost:9091";

    @Bean
    public WebClient webClient() {
        return WebClient.create();
    }

    @Bean
    public DiscoveryService discoveryService(WebClient webClient, ClientServiceConfigImpl clientServiceConfig,
                                             List<RSocketClientRequester> rSocketClientRequesters) {
        return new DiscoveryServiceImpl(webClient, clientServiceConfig, rSocketClientRequesters, DISCOVERY_URL);
    }
}
