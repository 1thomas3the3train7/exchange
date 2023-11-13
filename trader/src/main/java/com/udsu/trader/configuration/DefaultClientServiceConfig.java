package com.udsu.trader.configuration;

import com.discovery.service.client.configuration.ClientServiceConfig;
import com.discovery.service.model.discovery.ServiceInfo;
import com.discovery.service.model.discovery.ServiceType;
import org.springframework.stereotype.Component;

@Component
public class DefaultClientServiceConfig extends ClientServiceConfig {
    @Override
    public void configInit() {
        this.address = "localhost";
        this.port = 8087;
    }

    @Override
    public ServiceInfo buildInfo() {
        return ServiceInfo.builder()
                .serviceType(ServiceType.TRADE)
                .port(this.port)
                .rSocketPort(this.rSocketPort)
                .build();
    }
}
