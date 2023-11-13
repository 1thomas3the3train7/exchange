package com.udsu.updater.rsocket.server;

import com.google.gson.Gson;
import com.udsu.updater.model.entity.BankAccount;
import com.udsu.updater.model.request.BankAccountRequest;
import com.udsu.updater.model.rsocket.RSocketModel;
import com.udsu.updater.service.bankaccount.BankAccountResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BankAccountController {
    private final BankAccountResponseService bankAccountResponseService;
    private final Gson gson;

    @MessageMapping("/bankaccount/create")
    public Mono<String> bankAccountCreate(RSocketModel<BankAccountRequest> bankAccountRequestRSocketModel) {
        return bankAccountResponseService.createBankAccountResponse(bankAccountRequestRSocketModel.getPayload())
                .map(response -> gson.toJson(RSocketModel.<BankAccount>builder()
                        .payload(response)
                        .build()));
    }

    @MessageMapping("/bankaccount/find/by/user")
    public Mono<String> bankAccountByUser(RSocketModel<BankAccountRequest> bankAccountRequestRSocketModel) {
        return bankAccountResponseService.findBankAccountsByUserId(bankAccountRequestRSocketModel.getPayload())
                .map(response -> gson.toJson(RSocketModel.<List<BankAccount>>builder()
                        .payload(response)
                        .build()));
    }
}
