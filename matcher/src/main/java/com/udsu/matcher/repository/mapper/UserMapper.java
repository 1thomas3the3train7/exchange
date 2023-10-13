package com.udsu.matcher.repository.mapper;

import com.udsu.matcher.model.entity.BankAccount;
import com.udsu.matcher.model.entity.User;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.springframework.stereotype.Component;

import java.util.List;

public class UserMapper extends BaseMapper {
    public static User map(Row row, RowMetadata rowMetadata) {
        final String id = getStringVal("id", "user_id", row, rowMetadata);
        final String token = getStringVal("token", "user_token", row, rowMetadata);

        return User.builder()
                .id(id)
                .token(token)
                .build();
    }

    public static User mapWithBankAccount(Row row, RowMetadata rowMetadata) {
        final User user = map(row, rowMetadata);

        final BankAccount bankAccount = BankAccountMapper.map(row, rowMetadata);

        if (bankAccount != null)
            user.setBankAccounts(List.of(bankAccount));
        return user;
    }

    public static User mapListToOne(List<User> users) {
        final User firstUser = users.getFirst();
        return User.builder()
                .id(firstUser.getId())
                .token(firstUser.getToken())
                .bankAccounts(users.stream()
                        .filter(u -> u.getBankAccounts() != null && !u.getBankAccounts().isEmpty())
                        .map(u -> u.getBankAccounts().get(0))
                        .toList())
                .build();
    }
}
