package com.udsu.auth.controller;

import com.udsu.auth.model.ControllerModel;
import com.udsu.auth.model.Order;
import com.udsu.auth.model.request.OrderByUserRequest;
import com.udsu.auth.service.route.OrderRouter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderRouter orderRouter;

    @PostMapping("/find/by/user")
    public Mono<ControllerModel<List<Order>>> findByUser(@RequestBody OrderByUserRequest order) {
        log.info("FIND BY USER, {}", order);
        return orderRouter.findByUser(order);
    }
}
