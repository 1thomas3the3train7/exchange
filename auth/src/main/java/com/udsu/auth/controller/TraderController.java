package com.udsu.auth.controller;

import com.udsu.auth.model.buy.BuyRequest;
import com.udsu.auth.model.buy.BuyResponse;
import com.udsu.auth.model.sell.SellRequest;
import com.udsu.auth.model.sell.SellResponse;
import com.udsu.auth.service.route.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/trade")
@RequiredArgsConstructor
public class TraderController {
    private final RouteService routeService;

    @PostMapping("/buy")
    public Mono<BuyResponse> buyRequest(@RequestBody BuyRequest buyRequest) {
        return routeService.routeToBuy(buyRequest);
    }

    @PostMapping("/sell")
    public Mono<SellResponse> sellRequest(@RequestBody SellRequest sellRequest) {
        return routeService.routeToSell(sellRequest);
    }
}
