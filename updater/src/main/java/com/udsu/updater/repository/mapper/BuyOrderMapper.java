package com.udsu.updater.repository.mapper;

import com.udsu.updater.model.Currency;
import com.udsu.updater.model.entity.BuyOrder;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.time.LocalDateTime;

public class BuyOrderMapper extends BaseMapper{
    public static BuyOrder map(Row row, RowMetadata rowMetadata) {
        final String id = getStringVal("id", row, rowMetadata);
        final String currencyToBuy = getStringVal("currency_to_buy", row, rowMetadata);
        final String currencyToSell = getStringVal("currency_to_sell", row, rowMetadata);
        final Long countToBuy = getLongVal("count_to_buy", row, rowMetadata);
        final Long countToSell = getLongVal("count_to_sell", row, rowMetadata);
        final Float maxPrice = getFloatVal("max_price", row, rowMetadata);
        final Long ownerId = getLongVal("owner_id", row, rowMetadata);
        final Long fromBankAccountId = getLongVal("from_bank_account_id", row, rowMetadata);
        final Long toBankAccountId = getLongVal("to_bank_account_id", row, rowMetadata);
        final LocalDateTime datePublication = getLocalDateTimeVal("date_publication", row, rowMetadata);
        final LocalDateTime dateUpdate = getLocalDateTimeVal("date_update", row, rowMetadata);
        final Boolean active = getBoolVal("active", row, rowMetadata);
        final Boolean isEnough = getBoolVal("isenough", row, rowMetadata);

        return BuyOrder.builder()
                .id(id)
                .currencyToBuy(Currency.valueOf(currencyToBuy))
                .currencyToSell(Currency.valueOf(currencyToSell))
                .countToBuy(countToBuy)
                .countToSell(countToSell)
                .maxPrice(maxPrice)
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
