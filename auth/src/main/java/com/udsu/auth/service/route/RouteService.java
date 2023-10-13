package com.udsu.auth.service.route;

import com.udsu.auth.model.buy.BuyRequest;
import com.udsu.auth.model.buy.BuyResponse;
import com.udsu.auth.model.sell.SellRequest;
import com.udsu.auth.model.sell.SellResponse;
import reactor.core.publisher.Mono;

public interface RouteService {
    Mono<SellResponse> routeToSell(SellRequest sellRequest);

    Mono<BuyResponse> routeToBuy(BuyRequest buyRequest);
}
