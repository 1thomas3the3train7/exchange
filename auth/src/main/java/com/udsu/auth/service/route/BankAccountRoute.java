package com.udsu.auth.service.route;

import com.udsu.auth.rsocket.client.BankAccountRSocketClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankAccountRoute {
    private final BankAccountRSocketClient bankAccountRSocketClient;
}
