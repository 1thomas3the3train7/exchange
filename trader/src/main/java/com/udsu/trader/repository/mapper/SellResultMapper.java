package com.udsu.trader.repository.mapper;

import com.udsu.trader.model.entity.SellResult;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.time.LocalDateTime;

public class SellResultMapper extends BaseMapper {
    public static SellResult map(Row row, RowMetadata rowMetadata) {
        final Long id = getLongVal("id", row, rowMetadata);
        final Long idOrderSell = getLongVal("id_order_buy", row, rowMetadata);
        final Long idBankOperation = getLongVal("id_bank_account", row, rowMetadata);
        final Float operationSum = getFloatVal("operation_sum", row, rowMetadata);
        final Float price = getFloatVal("price", row, rowMetadata);
        final LocalDateTime dateCreation = getLocalDateTimeVal("date_creation", row, rowMetadata);
        final boolean isFinalResult = getBoolVal("is_final_result", row, rowMetadata);
        final boolean isEnough = getBoolVal("is_enought", row, rowMetadata);
        return SellResult.builder()
                .id(id)
                .idOrderBuy(idOrderSell)
                .idBankAccount(idBankOperation)
                .operationSum(operationSum)
                .dateCreation(dateCreation)
                .price(price)
                .isFinalResult(isFinalResult)
                .isEnough(isEnough)
                .build();
    }
}
