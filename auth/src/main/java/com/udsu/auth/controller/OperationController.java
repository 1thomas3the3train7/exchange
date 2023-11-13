package com.udsu.auth.controller;

import com.udsu.auth.model.ControllerModel;
import com.udsu.auth.model.OperationHistory;
import com.udsu.auth.model.request.OperationByDateRequest;
import com.udsu.auth.service.route.OperationHistoryRoute;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1/operation")
@RequiredArgsConstructor
public class OperationController {
    private final OperationHistoryRoute operationHistoryRoute;

    @PostMapping("/find/by/datecurr")
    public Mono<ControllerModel<OperationHistory>> findByDateCurr(@RequestBody OperationByDateRequest operation) {
        log.info("FIND OPERATION BY DATE CURRENCY, {}", operation);
        return operationHistoryRoute.findByDateCurr(operation);
    }

    @PostMapping("/find/lastprice")
    public Mono<ControllerModel<OperationHistory>> findByLastPrice(@RequestBody OperationByDateRequest operation) {
        log.info("FIND OPERATION LAST PRICE, {}", operation);
        return operationHistoryRoute.findByDateCurr(operation);
    }
}
