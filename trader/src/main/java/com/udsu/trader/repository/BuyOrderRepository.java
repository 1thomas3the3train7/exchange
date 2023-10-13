package com.udsu.trader.repository;

import com.udsu.trader.model.User;
import com.udsu.trader.model.buy.BuyRequest;
import com.udsu.trader.model.entity.BuyOrder;
import com.udsu.trader.model.entity.BuyResult;
import com.udsu.trader.model.sell.SellRequest;
import com.udsu.trader.repository.mapper.BuyOrderMapper;
import com.udsu.trader.repository.mapper.BuyResultMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
@RequiredArgsConstructor
public class BuyOrderRepository {
    private final DatabaseClient orderDatabase;

    public Flux<BuyOrder> findBuyOrderForSellRequest(SellRequest sellRequest, User user) {
        final String sql = "SELECT * FROM find_buy_order_for_sell_request(:currencyToBuy, :currencyToSell, :countToSell, :minPrice, :userId)";
        return orderDatabase.sql(sql)
                .bind("currencyToBuy", sellRequest.getCurrencyToBuy())
                .bind("currencyToSell", sellRequest.getCurrencyToSell())
                .bind("countToSell", sellRequest.getCountToSell())
                .bind("minPrice", sellRequest.getMinPrice())
                .bind("userId", user.getId())
                .map(BuyOrderMapper::map)
                .all();
    }

    public Flux<BuyResult> buyOperation(BuyRequest buyRequest, Long ownerId, Long buyBankAccountId, Long orderBuyId) {
        final String sql = """
                    SELECT * FROM buy_operation(:currencyToBuy, :currencyToSell, :countToBuy, :maxPrice, :skipOwnerId, :buyAccountId, :orderBuyId)
                """;
        return orderDatabase.sql(sql)
                .bind("currencyToBuy", buyRequest.getCurrencyToBuy())
                .bind("currencyToSell", buyRequest.getCurrencyToSell())
                .bind("countToBuy", buyRequest.getCountToBuy())
                .bind("maxPrice", buyRequest.getMaxPrice())
                .bind("skipOwnerId", ownerId)
                .bind("buyAccountId", buyBankAccountId)
                .bind("orderBuyId", orderBuyId)
                .map(BuyResultMapper::map)
                .all();
    }
}
