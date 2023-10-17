package com.udsu.matcher.condiguration;

import com.discovery.service.client.service.DiscoveryService;
import com.discovery.service.client.service.impl.DiscoveryServiceImpl;
import com.discovery.service.client.service.rsocket.RSocketClientRequester;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Configuration
public class DiscoveryServiceConfig {
    @Bean
    public DiscoveryService discoveryService(WebClient webClient,
                                             DefaultClientServiceConfig defaultClientServiceConfig,
                                             List<RSocketClientRequester> rSocketClientRequesters,
                                             @Value("${matcher.discovery.url}") String url) {
        DiscoveryServiceImpl discoveryService = DiscoveryServiceImpl.builder()
                .discoveryServiceConfig(defaultClientServiceConfig)
                .DISCOVERY_URL(url)
                .webClient(webClient)
                .rSocketClientRequesters(rSocketClientRequesters)
                .build();
        return discoveryService;
    }
}
