package com.udsu.updater.configuration;

import io.netty.buffer.ByteBufAllocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.NettyDataBufferFactory;

@Configuration
public class NettyDataBufferConfig {
    @Bean
    public NettyDataBufferFactory nettyDataBuffer() {
        return new NettyDataBufferFactory(ByteBufAllocator.DEFAULT);
    }
}
