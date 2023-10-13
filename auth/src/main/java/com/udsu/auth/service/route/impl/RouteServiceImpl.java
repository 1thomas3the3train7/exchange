package com.udsu.auth.service.route.impl;

import com.udsu.auth.model.buy.BuyRequest;
import com.udsu.auth.model.buy.BuyResponse;
import com.udsu.auth.model.sell.SellRequest;
import com.udsu.auth.model.sell.SellResponse;
import com.udsu.auth.service.kafka.KafkaService;
import com.udsu.auth.service.route.RouteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {
    private final KafkaService kafkaService;

    @Override
    public Mono<SellResponse> routeToSell(SellRequest sellRequest) {
        return kafkaService.sendToSellTopic(sellRequest);
    }

    @Override
    public Mono<BuyResponse> routeToBuy(BuyRequest buyRequest) {
        return kafkaService.sendToBuyTopic(buyRequest);
    }
}
