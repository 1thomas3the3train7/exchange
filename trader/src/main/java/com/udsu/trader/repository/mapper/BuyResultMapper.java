package com.udsu.trader.repository.mapper;

import com.udsu.trader.model.entity.BuyResult;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class BuyResultMapper extends BaseMapper {
    public static BuyResult map(Row row, RowMetadata rowMetadata) {
        final Long id = getLongVal("id", row, rowMetadata);
        final Long idOrderSell = getLongVal("id_order_sell", row, rowMetadata);
        final Long idBankOperation = getLongVal("id_bank_account", row, rowMetadata);
        final Float operationSum = getFloatVal("operation_sum", row, rowMetadata);
        final boolean isFinalResult = getBoolVal("is_final_result", row, rowMetadata);
        final boolean isEnough = getBoolVal("is_enought", row, rowMetadata);
        return BuyResult.builder()
                .id(id)
                .idOrderSell(idOrderSell)
                .idBankAccount(idBankOperation)
                .operationSum(operationSum)
                .isFinalResult(isFinalResult)
                .isEnough(isEnough)
                .build();
    }
}
