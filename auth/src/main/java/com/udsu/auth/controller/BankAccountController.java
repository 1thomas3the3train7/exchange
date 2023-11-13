package com.udsu.auth.controller;

import com.udsu.auth.model.ControllerModel;
import com.udsu.auth.model.request.BankAccountRequest;
import com.udsu.auth.service.route.BankAccountRoute;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/bankaccount")
@RequiredArgsConstructor
public class BankAccountController {
    private final BankAccountRoute bankAccountRoute;
    @PostMapping("/create")
    public Mono<ControllerModel> create(@RequestBody BankAccountRequest bankAccountRequest) {
        return null;
    }
}
