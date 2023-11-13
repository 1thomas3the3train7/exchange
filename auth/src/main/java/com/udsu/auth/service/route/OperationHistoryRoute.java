package com.udsu.auth.service.route;

import com.udsu.auth.model.ControllerModel;
import com.udsu.auth.model.OperationHistory;
import com.udsu.auth.model.request.LastPriceRequest;
import com.udsu.auth.model.request.OperationByDateRequest;
import com.udsu.auth.rsocket.client.OperationRSocketClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OperationHistoryRoute {
    private final OperationRSocketClient operationRSocketClient;

    public Mono<ControllerModel<OperationHistory>> findByDateCurr(OperationByDateRequest operation) {
        return operationRSocketClient.findDateCurr(operation)
                .map(operationHistoryRSocketModel -> ControllerModel.<OperationHistory>builder()
                        .payload(operationHistoryRSocketModel.getPayload())
                        .build());
    }

    public Mono<ControllerModel<OperationHistory>> findLastPrice(LastPriceRequest lastPriceRequest) {
        return operationRSocketClient.findLastPrice(lastPriceRequest)
                .map(operationHistoryRSocketModel -> ControllerModel.<OperationHistory>builder()
                        .payload(operationHistoryRSocketModel.getPayload())
                        .build());
    }
}
