package com.udsu.updater.repository;

import com.udsu.updater.model.OperationHistory;
import com.udsu.updater.model.entity.BuyOrder;
import com.udsu.updater.model.entity.SellOrder;
import com.udsu.updater.repository.mapper.BuyOrderMapper;
import com.udsu.updater.repository.mapper.SellOrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

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
}
