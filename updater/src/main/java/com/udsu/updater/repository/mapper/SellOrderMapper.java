package com.udsu.updater.repository.mapper;

import com.udsu.updater.model.Currency;
import com.udsu.updater.model.entity.SellOrder;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.time.LocalDateTime;

public class SellOrderMapper extends BaseMapper{
    public static SellOrder map(Row row, RowMetadata rowMetadata) {
        final String id = getStringVal("id", row, rowMetadata);
        final String currencyToBuy = getStringVal("currency_to_buy", row, rowMetadata);
        final String currencyToSell = getStringVal("currency_to_sell", row, rowMetadata);
        final Long countToBuy = getLongVal("count_to_buy", row, rowMetadata);
        final Long countToSell = getLongVal("count_to_sell", row, rowMetadata);
        final Long minPrice = getLongVal("min_price", row, rowMetadata);
        final Long ownerId = getLongVal("owner_id", row, rowMetadata);
        final Long fromBankAccountId = getLongVal("from_bank_account_id", row, rowMetadata);
        final Long toBankAccountId = getLongVal("to_bank_acount_id", row, rowMetadata);
        final LocalDateTime datePublication = getLocalDateTimeVal("date_publication", row, rowMetadata);
        final LocalDateTime dateUpdate = getLocalDateTimeVal("date_update", row, rowMetadata);
        final boolean active = getBoolVal("active", row, rowMetadata);
        final boolean isEnough = getBoolVal("isenough", row, rowMetadata);

        return SellOrder.builder()
                .id(id)
                .currencyToBuy(Currency.valueOf(currencyToBuy))
                .currencyToSell(Currency.valueOf(currencyToSell))
                .countToBuy(countToBuy)
                .countToSell(countToSell)
                .minPrice(minPrice)
                .ownerId(ownerId)
                .fromBankAccount(fromBankAccountId)
                .toBankAccount(toBankAccountId)
                .datePublication(datePublication)
                .dateUpdate(dateUpdate)
                .active(active)
                .isenough(isEnough)
                .build();
    }
}
