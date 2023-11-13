package com.udsu.updater.rsocket.server;

import com.google.gson.Gson;
import com.udsu.updater.model.Order;
import com.udsu.updater.model.request.OrderByUserRequest;
import com.udsu.updater.model.rsocket.RSocketModel;
import com.udsu.updater.service.order.OrderResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderResponseService orderResponseService;
    private final Gson gson;

    @MessageMapping("/order/find/user")
    public Mono<String> orderByUserResponse(RSocketModel<OrderByUserRequest> order) {
        return orderResponseService.orderByUserResponse(order.getPayload())
                .map(response -> gson.toJson(RSocketModel.<List<Order>>builder()
                        .payload(response)
                        .build()));
    }
}
