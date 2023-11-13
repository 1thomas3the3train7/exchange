package com.udsu.updater.service.bankaccount;

import com.udsu.updater.model.entity.BankAccount;
import com.udsu.updater.model.request.BankAccountRequest;
import com.udsu.updater.repository.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankAccountResponseService {
    private final BankAccountRepository bankAccountRepository;

    public Mono<BankAccount> createBankAccountResponse(BankAccountRequest bankAccountRequest) {
        return bankAccountRepository.createBankAccount(BankAccount.builder()
                .owner_id(bankAccountRequest.getUserId())
                .currency(bankAccountRequest.getCurrency())
                .build());
    }

    public Mono<List<BankAccount>> findBankAccountsByUserId(BankAccountRequest bankAccountRequest) {
        return bankAccountRepository.findBankAccountsByUser(bankAccountRequest.getUserId())
                .collectList();
    }
}
