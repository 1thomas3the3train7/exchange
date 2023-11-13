package com.udsu.auth.rsocket.client;

import com.discovery.service.client.service.rsocket.RSocketClientRequester;
import com.discovery.service.model.discovery.ServiceType;
import com.google.gson.Gson;
import com.udsu.auth.model.BankAccount;
import com.udsu.auth.model.request.BankAccountRequest;
import com.udsu.auth.model.rsocket.RSocketModel;
import com.udsu.auth.utils.GsonUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class BankAccountRSocketClient extends RSocketClientRequester {
    private final Gson gson;

    public BankAccountRSocketClient(Gson gson) {
        super(gson, ServiceType.UPDATER);
        this.gson = gson;
    }

    public Mono<RSocketModel<BankAccount>> createBankAccount(BankAccountRequest bankAccountRequest) {
        final RSocketModel<BankAccountRequest> rSocketModel = RSocketModel.<BankAccountRequest>builder()
                .payload(bankAccountRequest)
                .build();
        return this.rSocketRequester
                .flatMap(rs -> rs.route("/bankaccount/create")
                        .data(gson.toJson(rSocketModel))
                        .retrieveMono(String.class))
                .map(response -> gson.fromJson(response, GsonUtils.rSocketModelBankAccount));
    }

    public Mono<RSocketModel<List<BankAccount>>> findByUser(BankAccountRequest bankAccountRequest) {
        final RSocketModel<BankAccountRequest> rSocketModel = RSocketModel.<BankAccountRequest>builder()
                .payload(bankAccountRequest)
                .build();
        return this.rSocketRequester
                .flatMap(rs -> rs.route("/bankaccount/find/by/user")
                        .data(gson.toJson(rSocketModel))
                        .retrieveMono(String.class))
                .map(response -> gson.fromJson(response, GsonUtils.rSocketModelListBankAccount));
    }
}
