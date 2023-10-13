package com.udsu.trader.configuration;

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
                                             @Value("${trader.discovery.url}") String url) {
        DiscoveryServiceImpl discoveryService = new DiscoveryServiceImpl(webClient, defaultClientServiceConfig,
                rSocketClientRequesters, url);
        return discoveryService;
    }
}
