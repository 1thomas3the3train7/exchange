package com.udsu.updater.repository;

import com.udsu.updater.model.entity.BankAccount;
import com.udsu.updater.repository.mapper.BankAccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class BankAccountRepository {
    private final DatabaseClient orderDatabase;

    public Mono<BankAccount> createBankAccount(BankAccount bankAccount) {
        final String sql = "INSERT INTO bank_account (currency, balance, owner_id) VALUES (:currency, 0, :ownerId) RETURNING id";
        return orderDatabase.sql(sql)
                .bind("currency", bankAccount.getCurrency())
                .bind("ownerId", bankAccount.getOwner_id())
                .map(r -> {
                    bankAccount.setId((String) r.get("id"));
                    return bankAccount;
                })
                .one();
    }

    public Mono<BankAccount> upBalanceBankAccount(Long bankAccountId, Float upBalance) {
        final String sql = "UPDATE bank_account SET balance = balance + :upBalance WHERE id = :bankAccountId RETURNING *";
        return orderDatabase.sql(sql)
                .bind("upBalance", upBalance)
                .bind("bankAccountId", bankAccountId)
                .map(BankAccountMapper::map)
                .one();
    }
}
