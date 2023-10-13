package com.udsu.trader.repository;

import com.udsu.trader.model.User;
import com.udsu.trader.model.buy.BuyRequest;
import com.udsu.trader.model.entity.SellOrder;
import com.udsu.trader.model.entity.SellResult;
import com.udsu.trader.model.sell.SellRequest;
import com.udsu.trader.repository.mapper.SellOrderMapper;
import com.udsu.trader.repository.mapper.SellResultMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
@RequiredArgsConstructor
public class SellOrderRepository {
    private final DatabaseClient orderDatabase;

    public Flux<SellOrder> findSellOrderForBuyRequest(BuyRequest buyRequest, User user) {
        final String sql = "SELECT * FROM find_sell_order_for_buy_request(:currencyToBuy, :currencyToSell, :countToBuy, :maxPrice, :userId)";
        return orderDatabase.sql(sql)
                .bind("currencyToBuy", buyRequest.getCurrencyToBuy())
                .bind("currencyToSell", buyRequest.getCurrencyToSell())
                .bind("countToBuy", buyRequest.getCountToBuy())
                .bind("userId", user.getId())
                .map(SellOrderMapper::map)
                .all();
    }

    public Flux<SellResult> sellOperation(SellRequest sellRequest, Long ownerId, Long sellBankAccountId, Long orderSellId) {
        final String sql = """
                    SELECT * FROM sell_operation(:currencyToBuy, :currencyToSell, :countToSell, :minPrice, :skipOwnerId,
                    :bankAccountSellId, :sellOrderId)
                """;
        return orderDatabase.sql(sql)
                .bind("currencyToBuy", sellRequest.getCurrencyToBuy())
                .bind("currencyToSell", sellRequest.getCurrencyToSell())
                .bind("countToSell", sellRequest.getCountToSell())
                .bind("minPrice", sellRequest.getMinPrice())
                .bind("skipOwnerId", ownerId)
                .bind("bankAccountSellId", sellBankAccountId)
                .bind("sellOrderId", orderSellId)
                .map(SellResultMapper::map)
                .all();
    }
}
