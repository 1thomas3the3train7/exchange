package com.udsu.updater.repository;

import com.udsu.updater.model.entity.User;
import com.udsu.updater.repository.mapper.BankAccountMapper;
import com.udsu.updater.repository.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final DatabaseClient userDatabase;

    public Mono<User> findByIdWithBankAccounts(Long id) {
        final String sql = "SELECT u.id as user_id, u.token as user_token, ba.id as bank_account_id, ba.owner_id as bank_account_owner_id, " +
                "ba.balance as bank_account_balance, ba.currency as bank_account_currency " +
                "FROM users u LEFT JOIN bank_account ba ON u.id = ba.owner_id WHERE u.id = :id";
        return userDatabase.sql(sql)
                .bind("id", id)
                .map(UserMapper::mapWithBankAccount)
                .all()
                .collectList()
                .map(UserMapper::mapListToOne);

    }
}
