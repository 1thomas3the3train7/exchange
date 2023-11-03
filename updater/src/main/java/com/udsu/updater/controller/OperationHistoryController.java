package com.udsu.updater.controller;

import com.udsu.updater.model.OperationHistory;
import com.udsu.updater.model.request.LastPriceRequest;
import com.udsu.updater.model.request.OperationByDateRequest;
import com.udsu.updater.service.operation.OperationResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/operation")
@RequiredArgsConstructor
public class OperationHistoryController {
    private final OperationResponseService operationResponseService;
    @PostMapping("/find/price")
    public Mono<OperationHistory> findPrice(@RequestBody LastPriceRequest lastPriceRequest) {
        return operationResponseService.buildFindLastPriceResponse(lastPriceRequest);
    }

    @PostMapping("/find/date")
    public Mono<List<OperationHistory>> findDate(@RequestBody OperationByDateRequest operation) {
        return operationResponseService.buildFindByDateAndCurrResponse(operation);
    }
}