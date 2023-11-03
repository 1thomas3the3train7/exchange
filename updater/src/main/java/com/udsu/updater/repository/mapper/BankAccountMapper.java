package com.udsu.updater.repository.mapper;

import com.udsu.updater.model.Currency;
import com.udsu.updater.model.entity.BankAccount;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class BankAccountMapper extends BaseMapper {
    public static BankAccount map(Row row, RowMetadata rowMetadata) {
        final String id = getStringVal("id", "bank_account_id", row, rowMetadata);
        final String currency = getStringVal("currency", "bank_account_currency", row, rowMetadata);
        final Long owner_id = getLongVal("owner_id", "bank_account_owner_id", row, rowMetadata);
        final Long balance = getLongVal("balance", "bank_account_balance", row, rowMetadata);

        return BankAccount.builder()
                .id(id)
                .currency(Currency.valueOf(currency.toLowerCase()))
                .balance(balance)
                .owner_id(owner_id)
                .build();
    }
}
