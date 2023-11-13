package com.udsu.auth.rsocket.client;

import com.discovery.service.client.service.rsocket.RSocketClientRequester;
import com.discovery.service.model.discovery.ServiceType;
import com.google.gson.Gson;
import com.udsu.auth.model.Order;
import com.udsu.auth.model.request.OrderByUserRequest;
import com.udsu.auth.model.rsocket.RSocketModel;
import com.udsu.auth.utils.GsonUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class OrderRSocketClient extends RSocketClientRequester {
    private final Gson gson;

    public OrderRSocketClient(Gson gson) {
        super(gson, ServiceType.UPDATER);
        this.gson = gson;
    }

    public Mono<RSocketModel<List<Order>>> findByUser(OrderByUserRequest order) {
        final RSocketModel<OrderByUserRequest> requestRSocketModel = RSocketModel.<OrderByUserRequest>builder()
                .payload(order)
                .build();
        return rSocketRequester
                .flatMap(req -> req
                        .route("/order/find/user")
                        .data(gson.toJson(requestRSocketModel))
                        .retrieveMono(String.class))
                .map(res -> gson.fromJson(res, GsonUtils.rSocketModelListOrder));
    }
}
