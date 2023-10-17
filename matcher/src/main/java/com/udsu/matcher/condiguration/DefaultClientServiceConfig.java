package com.udsu.matcher.condiguration;

import com.discovery.service.client.configuration.ClientServiceConfig;
import com.discovery.service.model.discovery.ServiceInfo;
import com.discovery.service.model.discovery.ServiceType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DefaultClientServiceConfig extends ClientServiceConfig{
    @Override
    public void configInit() {
        this.address = "localhost";
        this.port = 8084;
        this.serviceTypesToConnected = List.of(ServiceType.TRADE);
    }

    @Override
    public ServiceInfo buildInfo() {
        return ServiceInfo.builder()
                .address(this.address)
                .port(this.port)
                .serviceType(ServiceType.MATCHER)
                .serviceToConnected(List.of(ServiceInfo.builder().serviceType(ServiceType.TRADE).build()))
                .build();
    }
}
