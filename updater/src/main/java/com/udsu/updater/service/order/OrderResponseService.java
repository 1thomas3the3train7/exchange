package com.udsu.updater.service.order;

import com.udsu.updater.model.Order;
import com.udsu.updater.model.entity.BuyOrder;
import com.udsu.updater.model.request.OrderByUserRequest;
import com.udsu.updater.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderResponseService {
    private final OrderRepository orderRepository;

    public Mono<List<Order>> orderByUserResponse(OrderByUserRequest orderByUserRequest) {
        final Flux<Order> buyOrderFlux = orderRepository.findBuyOrderByUser(orderByUserRequest.getUserId())
                .map(buyOrder -> buyOrder.toOrder(buyOrder.getMaxPrice()));
        final Flux<Order> sellOrderFlux = orderRepository.findSellOrderByUser(orderByUserRequest.getUserId())
                .map(sellOrder -> sellOrder.toOrder(sellOrder.getMinPrice()));
        return buyOrderFlux.mergeWith(sellOrderFlux)
                .collectList();
    }
}
