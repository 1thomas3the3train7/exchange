package com.udsu.updater.repository;

import com.udsu.updater.model.Currency;
import com.udsu.updater.model.OperationHistory;
import com.udsu.updater.model.OperationStatus;
import com.udsu.updater.model.entity.BuyOrder;
import com.udsu.updater.model.entity.SellOrder;
import com.udsu.updater.model.request.LastPriceRequest;
import com.udsu.updater.repository.mapper.BuyOrderMapper;
import com.udsu.updater.repository.mapper.OperationHistoryMapper;
import com.udsu.updater.repository.mapper.SellOrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class OperationHistoryRepository {
    private final DatabaseClient orderDatabase;

    public Mono<BuyOrder> findBuyOrderById(Long id) {
        return orderDatabase.sql("SELECT * FROM order_buy WHERE id = :id")
                .bind("id", id)
                .map(BuyOrderMapper::map)
                .one();
    }

    public Mono<SellOrder> findSellOrderById(Long id) {
        return orderDatabase.sql("SELECT * FROM order_sell WHERE id = :id")
                .bind("id", id)
                .map(SellOrderMapper::map)
                .one();
    }

    public Mono<OperationHistory> findLastPriceForCurrency(LastPriceRequest lastPriceRequest) {
        final String sql = """
                    SELECT oh.id, count_in_operation, operation_status, oh.to_bank_account_id, order_id, price, date_creation
                    FROM operation_history oh INNER JOIN order_sell os ON oh.order_id = os.id
                    WHERE os.currency_to_sell = :currencyToSell AND os.currency_to_buy = :currencyToBuy
                """;
        return orderDatabase.sql(sql)
                .bind("currencyToSell", lastPriceRequest.getCurrencyToSell().toString())
                .bind("currencyToBuy", lastPriceRequest.getCurrencyToBuy().toString())
                .map(OperationHistoryMapper::map)
                .one();
    }

    public Flux<OperationHistory> findByDate(LocalDateTime from, LocalDateTime to, Currency fromCurr, Currency toCurr, OperationStatus operationStatus) {
        final String sql = """
                    SELECT * FROM find_operation_by_date_and_currency(:from, :to, :currencyToBuy, :currencyToSell, :orderType)
                """;
        return orderDatabase.sql(sql)
                .bind("from", from)
                .bind("to", to)
                .bind("currencyToBuy", fromCurr.toString())
                .bind("currencyToSell", fromCurr.toString())
                .bind("orderType", operationStatus.toString())
                .map(OperationHistoryMapper::map)
                .all();
    }
}
