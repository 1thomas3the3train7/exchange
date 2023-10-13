package com.udsu.matcher.repository.impl;

import com.udsu.matcher.model.Currency;
import com.udsu.matcher.model.entity.User;
import com.udsu.matcher.repository.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final DatabaseClient userDatabase;

    public Mono<User> findUserAndBankAccountByTokenAndAccountsIdAndCurrency(User user,
                                                                            String bankAccountIdToBuy,
                                                                            String bankAccountIdToSell,
                                                                            Currency currencyToBuy,
                                                                            Currency currencyToSell) {
        final String userCheck = """
                    SELECT u.id as user_id, u.token as user_token, ba.id as bank_account_id,
                    ba.owner_id as bank_account_owner_id, ba.balance as bank_account_balance,
                    ba.currency as bank_account_currency
                    FROM users u INNER JOIN bank_account ba ON u.id = ba.owner_id
                    WHERE ((ba.id = :bankAccountIdToSell AND ba.currency = :currencyToSell)
                    OR (ba.id = :bankAccountIdToBuy AND ba.currency = :currencyToBuy)) AND u.token = :token
                """;
        return userDatabase.sql(userCheck)
                .bind("token", user.getToken())
                .bind("bankAccountIdToBuy", bankAccountIdToBuy)
                .bind("bankAccountIdToSell", bankAccountIdToSell)
                .bind("currencyToSell", currencyToSell)
                .bind("currencyToBuy", currencyToBuy)
                .map((UserMapper::mapWithBankAccount))
                .all()
                .collectList()
                .map(UserMapper::mapListToOne);
    }
}
