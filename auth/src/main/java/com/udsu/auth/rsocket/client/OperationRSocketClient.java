package com.udsu.auth.rsocket.client;

import com.discovery.service.client.service.rsocket.RSocketClientRequester;
import com.discovery.service.model.discovery.ServiceType;
import com.google.gson.Gson;
import com.udsu.auth.model.OperationHistory;
import com.udsu.auth.model.request.LastPriceRequest;
import com.udsu.auth.model.request.OperationByDateRequest;
import com.udsu.auth.model.rsocket.RSocketModel;
import com.udsu.auth.utils.GsonUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class OperationRSocketClient extends RSocketClientRequester {
    private final Gson gson;
    public OperationRSocketClient(Gson gson) {
        super(gson, ServiceType.UPDATER);
        this.gson = gson;
    }

    public Mono<RSocketModel<OperationHistory>> findDateCurr(OperationByDateRequest operation) {
        final RSocketModel<OperationByDateRequest> rSocketModel = RSocketModel.<OperationByDateRequest>builder()
                .payload(operation)
                .build();
        return this.rSocketRequester
                .flatMap(rs -> rs.route("/operation/find/date-curr")
                        .data(gson.toJson(rSocketModel))
                        .retrieveMono(String.class))
                .map(response -> gson.fromJson(response, GsonUtils.rSocketModelOperationHistory));
    }

    public Mono<RSocketModel<OperationHistory>> findLastPrice(LastPriceRequest lastPriceRequest) {
        final RSocketModel<LastPriceRequest> rSocketModel = RSocketModel.<LastPriceRequest>builder()
                .payload(lastPriceRequest)
                .build();
        return this.rSocketRequester
                .flatMap(rs -> rs.route("/operation/find/last-price")
                        .data(gson.toJson(rSocketModel))
                        .retrieveMono(String.class))
                .map(response -> gson.fromJson(response, GsonUtils.rSocketModelOperationHistory));
    }
}
