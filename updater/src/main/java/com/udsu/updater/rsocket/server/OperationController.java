package com.udsu.updater.rsocket.server;

import com.google.gson.Gson;
import com.udsu.updater.model.OperationHistory;
import com.udsu.updater.model.request.LastPriceRequest;
import com.udsu.updater.model.request.OperationByDateRequest;
import com.udsu.updater.model.rsocket.RSocketModel;
import com.udsu.updater.service.operation.OperationResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OperationController {
    private final OperationResponseService operationResponseService;
    private final Gson gson;

    @MessageMapping("/operation/find/date-curr")
    public Mono<String> findByDateCurr(RSocketModel<OperationByDateRequest> operation) {
        return operationResponseService.buildFindByDateAndCurrResponse(operation.getPayload())
                .map(response -> gson.toJson(RSocketModel.<List<OperationHistory>>builder().payload(response).build()));
    }

    @MessageMapping("/operation/find/last-price")
    public Mono<String> findLastPrice(RSocketModel<LastPriceRequest> lastPriceRequestRSocketModel) {
        return operationResponseService.buildFindLastPriceResponse(lastPriceRequestRSocketModel.getPayload())
                .map(response -> gson.toJson(RSocketModel.<OperationHistory>builder().payload(response).build()));
    }
}
