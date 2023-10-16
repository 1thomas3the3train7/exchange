package com.udsu.updater.repository.mapper;

import com.udsu.updater.model.OperationHistory;
import com.udsu.updater.model.OperationStatus;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.time.LocalDateTime;

public class OperationHistoryMapper extends BaseMapper {
    public static OperationHistory map(Row row, RowMetadata rowMetadata) {
        final Long id = getLongVal("id", row, rowMetadata);
        final Float countInOperation = getFloatVal("count_in_operation", row, rowMetadata);
        final String operationStatus = getStringVal("operation_status", row, rowMetadata);
        final Long toBankAccountId = getLongVal("to_bank_account_id", row, rowMetadata);
        final Long orderId = getLongVal("order_id", row, rowMetadata);
        final Float price = getFloatVal("price", row, rowMetadata);
        final LocalDateTime dateCreation = getLocalDateTimeVal("date_creation", row, rowMetadata);
        return OperationHistory.builder()
                .id(id)
                .countInOperation(countInOperation)
                .operationStatus(operationStatus.toLowerCase().contains("buy") ? OperationStatus.BUY : OperationStatus.SELL)
                .toBankAccountId(toBankAccountId)
                .orderId(orderId)
                .price(price)
                .dateCreation(dateCreation)
                .build();
    }
}
