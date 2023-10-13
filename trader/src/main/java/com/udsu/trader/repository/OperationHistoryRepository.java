package com.udsu.trader.repository;

import com.udsu.trader.model.entity.OperationHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class OperationHistoryRepository {
    private final DatabaseClient orderDatabase;

    public Mono<OperationHistory> save(OperationHistory operationHistory) {
        final String sql = """
                    INSERT INTO operation_history (count_in_operation, operation_status, to_bank_account_id, order_id, price, date_creation) 
                    VALUES (:countInOperation, :operationStatus, :toBankAccountId, :orderId, :price, :dateCreation)
                    RETURNING id;
                """;
        return orderDatabase.sql(sql)
                .bind("countInOperation", operationHistory.getCountInOperation())
                .bind("operationStatus", operationHistory.getOperationStatus())
                .bind("toBankAccountId", operationHistory.getToBankAccountId())
                .bind("orderId", operationHistory.getOrderId())
                .bind("price", operationHistory.getPrice())
                .bind("dateCreation", operationHistory.getDateCreation())
                .map(readable -> {
                    operationHistory.setId((Long) readable.get("id"));
                    return operationHistory;
                })
                .one();
    }

}
