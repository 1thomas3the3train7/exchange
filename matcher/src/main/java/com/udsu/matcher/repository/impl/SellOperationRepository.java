package com.udsu.matcher.repository.impl;

import com.udsu.matcher.model.entity.BankAccount;
import com.udsu.matcher.model.entity.User;
import com.udsu.matcher.model.sell.SellRequest;
import com.udsu.matcher.repository.mapper.BankAccountMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SellOperationRepository {
    private final DatabaseClient orderDatabase;

    public Mono<BankAccount> lockBalance(SellRequest sellRequest) {
        final String sql = """
                    SELECT * FROM lock_balance(:bankAccountId, :count)
                """;
        return orderDatabase.sql(sql)
                .bind("bankAccountId", sellRequest.getFromBankAccount())
                .bind("count", sellRequest.getCountToSell())
                .map(BankAccountMapper::map)
                .one();
    }

    public Mono<Long> createOrderSell(SellRequest sellRequest, User user) {
        final String sql = """
                INSERT INTO order_sell (currency_to_sell, currency_to_buy, count_to_buy, count_to_sell, min_price, owner_id, from_bank_account_id, 
                to_bank_account_id, date_publication, date_update, active) 
                VALUES
                (:currencyToSell, :currencyToBuy, :countToBuy, :countToSell, :minPrice, :ownerId, 
                :fromBankAccount, :toBankAccount, :datePublication, :dateUpdate, :active)
                RETURNING id
                """;
        return orderDatabase.sql(sql)
                .bind("currencyToSell", sellRequest.getCurrencyToSell())
                .bind("currencyToBuy", sellRequest.getCurrencyToBuy())
                .bind("countToBuy", sellRequest.getCountToBuy())
                .bind("countToSell", sellRequest.getCountToSell())
                .bind("minPrice", sellRequest.getMinPrice())
                .bind("ownerId", user.getId())
                .bind("fromBankAccount", sellRequest.getToBankAccount())
                .bind("toBankAccount", sellRequest.getFromBankAccount())
                .bind("datePublication", LocalDateTime.now())
                .bind("dateUpdate", LocalDateTime.now())
                .bind("active", true)
                .map(readable -> (Long) readable.get("id"))
                .one();
    }
}
