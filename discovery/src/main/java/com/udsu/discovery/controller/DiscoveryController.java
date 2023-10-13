package com.udsu.discovery.controller;

import com.discovery.service.server.service.DiscoveryService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DiscoveryController extends com.discovery.service.server.controller.DiscoveryController {
    public DiscoveryController(DiscoveryService discoveryService) {
        super(discoveryService);
    }
}
