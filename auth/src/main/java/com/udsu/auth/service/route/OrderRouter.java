package com.udsu.auth.service.route;

import com.udsu.auth.model.ControllerModel;
import com.udsu.auth.model.Order;
import com.udsu.auth.model.request.OrderByUserRequest;
import com.udsu.auth.rsocket.client.OrderRSocketClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderRouter {
    private final OrderRSocketClient orderRSocketClient;
    public Mono<ControllerModel<List<Order>>> findByUser(OrderByUserRequest order) {
        return orderRSocketClient.findByUser(order)
                .map(response -> ControllerModel.<List<Order>>builder()
                        .payload(response.getPayload())
                        .build());
    }
}
