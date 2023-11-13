package com.udsu.auth.configuration;

import com.discovery.service.client.configuration.ClientServiceConfig;
import com.discovery.service.model.discovery.ServiceInfo;
import com.discovery.service.model.discovery.ServiceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClientServiceConfigImpl extends ClientServiceConfig {
    private final Environment environment;
    @Override
    public void configInit() {
        this.address = InetAddress.getLoopbackAddress().getHostAddress();
        try {
            this.port = Integer.parseInt(environment.getProperty("server.port"));
        } catch (Exception e) {
            log.warn("ERROR GET PORT, SET 8080, {}", e.getMessage());
            this.port = 8080;
        }
        try {
            this.rSocketPort = Integer.parseInt(environment.getProperty("spring.rsocket.server.port"));
        } catch (Exception e) {
            log.warn("ERROR GET RSOCKET PORT, {}", e.getMessage());
        }

        this.serviceTypesToConnected = List.of(ServiceType.UPDATER);
    }

    @Override
    public ServiceInfo buildInfo() {
        return ServiceInfo.builder()
                .address(this.address)
                .port(this.port)
                .rSocketPort(this.rSocketPort)
                .serviceType(ServiceType.AUTH)
                .serviceToConnected(serviceTypesToConnected.stream()
                        .map(f -> ServiceInfo.builder().serviceType(f).build())
                        .toList())
                .build();
    }
}
