package com.udsu.auth.controller;

import com.discovery.service.client.configuration.ClientServiceConfig;
import com.discovery.service.client.controller.ConfigController;
import com.discovery.service.client.service.DiscoveryService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeartbeatControllerImpl extends ConfigController {
    public HeartbeatControllerImpl(ClientServiceConfig clientServiceConfig, DiscoveryService discoveryService) {
        super(clientServiceConfig, discoveryService);
    }
}
