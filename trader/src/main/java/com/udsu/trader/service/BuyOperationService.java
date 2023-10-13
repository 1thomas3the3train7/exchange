package com.udsu.trader.service;

import com.udsu.trader.exception.SellOperationException;
import com.udsu.trader.model.User;
import com.udsu.trader.model.buy.BuyRequest;
import com.udsu.trader.model.buy.BuyResponse;
import com.udsu.trader.model.buy.StatusBuy;
import com.udsu.trader.model.entity.BuyResult;
import com.udsu.trader.repository.BuyOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class BuyOperationService {
    private final BuyOrderRepository buyOrderRepository;

    public Mono<BuyResponse> buy(BuyRequest buyRequest, User user) {
        return buyOrderRepository.buyOperation(buyRequest, user.getId(),
                        Long.parseLong(buyRequest.getToBankAccount()),
                        Long.parseLong(buyRequest.getToBankAccount()))
                .collectList()
                .handle((list, sink) -> {
                    if (list == null || list.isEmpty() || list.get(0).getId() == null) {
                        log.error("BUY RESULT IS EMPTY, {}, {}", buyRequest, user);
                        sink.error(new SellOperationException("RETURN EMPTY SELL OPERATION RESULT"));
                    }
                    final BuyResult first = list.getFirst();
                    final Float sold = list.stream().map(BuyResult::getOperationSum).max(Float::compareTo).get();
                    if (!first.isEnough()) {
                        sink.next(BuyResponse.builder()
                                .buyer(user)
                                .countWasBought(sold)
                                .status(StatusBuy.BUY_PART)
                                .build());
                    } else {
                        sink.next(BuyResponse.builder()
                                .countWasBought(sold)
                                .buyer(user)
                                .status(StatusBuy.COMPLETED)
                                .build());
                    }
                });
    }
}
