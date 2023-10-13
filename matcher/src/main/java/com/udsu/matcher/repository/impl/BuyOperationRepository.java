package com.udsu.matcher.repository.impl;

import com.udsu.matcher.exception.CreateOrderBuyException;
import com.udsu.matcher.model.buy.BuyRequest;
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
public class BuyOperationRepository {
    private final DatabaseClient orderDatabase;

    public Mono<BankAccount> lockBalance(BuyRequest buyRequest) {
        final String sql = """
                    SELECT * FROM lock_balance(:bankAccountId, :count)
                """;
        return orderDatabase.sql(sql)
                .bind("bankAccountId", buyRequest.getFromBankAccount())
                .bind("count", buyRequest.getCurrencyToBuy())
                .map(BankAccountMapper::map)
                .one();
    }

    public Mono<Long> createOrderBuy(BuyRequest buyRequest, User user) {
        final String sql = """
                INSERT INTO order_buy
                (currency_to_buy, currency_to_sell, count_to_buy, count_to_sell, max_price, owner_id, from_bank_account_id,
                to_bank_account_id, date_publication, date_update, active)
                VALUES (:currencyToBuy, :currencyToSell, :countToBuy, :countToSell, :maxPrice, :ownerId, :fromBankAccount,
                :toBankAccount, :datePublication, :dateUpdate, :active)
                RETURNING id
                """;
        return orderDatabase.sql(sql)
                .bind("currencyToBuy", buyRequest.getCurrencyToBuy())
                .bind("currencyToSell", buyRequest.getCurrencyToSell())
                .bind("countToBuy", buyRequest.getCountToBuy())
                .bind("countToSell", buyRequest.getCountToSell())
                .bind("maxPrice", buyRequest.getMaxPrice())
                .bind("ownerId", user.getId())
                .bind("fromBankAccount", buyRequest.getFromBankAccount())
                .bind("toBankAccount", buyRequest.getToBankAccount())
                .bind("datePublication", LocalDateTime.now())
                .bind("dateUpdate", LocalDateTime.now())
                .bind("active", true)
                .map(readable -> (Long) readable.get("id"))
                .one()
                .onErrorResume(throwable -> {
                    log.error("ERROR CREATE ORDER BUY IN DB, {}, {}, {}", throwable.getMessage(), buyRequest, user);
                    return Mono.error(new CreateOrderBuyException("ERROR CREATE ORDER BUY IN DB"));
                });
    }
}
