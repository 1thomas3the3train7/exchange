package com.udsu.trader.controller.rsocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
public class TradeRSocketController {
    @MessageMapping("/buy/trade")
    public Mono<byte[]> buyTrade(byte[] payload) {
        final String p = new String(payload);
        return Mono.just("wdwd".getBytes());
    }
}
