package com.udsu.trader.service;

import com.udsu.trader.exception.SellOperationException;
import com.udsu.trader.model.OperationResponse;
import com.udsu.trader.model.OperationStatus;
import com.udsu.trader.model.User;
import com.udsu.trader.model.entity.OperationHistory;
import com.udsu.trader.model.entity.SellResult;
import com.udsu.trader.model.sell.SellRequest;
import com.udsu.trader.model.sell.SellResponse;
import com.udsu.trader.model.sell.StatusSell;
import com.udsu.trader.repository.OperationHistoryRepository;
import com.udsu.trader.repository.SellOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class SellOperationService {
    private final SellOrderRepository sellOrderRepository;
    private final OperationHistoryRepository operationHistoryRepository;

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public Flux<OperationHistory> sell(SellRequest sellRequest, User user) {
        return sellOrderRepository.sellOperation(sellRequest, user.getId(),
                        Long.parseLong(sellRequest.getToBankAccount()),
                        Long.parseLong(sellRequest.getId()))
                .flatMap(sellResult -> operationHistoryRepository.save(OperationHistory.builder()
                                .orderId(sellResult.getIdOrderBuy())
                                .toBankAccountId(sellResult.getIdBankAccount())
                                .operationStatus(OperationStatus.SELL)
                                .countInOperation(sellResult.getOperationSum())
                                .price(sellResult.getPrice())
                                .dateCreation(sellResult.getDateCreation())
                                .build()));
//                .handle((list, sink) -> {
//                            if (list == null || list.isEmpty() || list.get(0).getId() == null) {
//                                log.error("SELL RESULT IS EMPTY, {}, {}", sellRequest, user);
//                                sink.error(new SellOperationException("RETURN EMPTY SELL OPERATION RESULT"));
//                            }
//                            final SellResult first = list.getFirst();
//                            final Float sold = list.stream().map(SellResult::getOperationSum).max(Float::compareTo).get();
//                            if (!first.isEnough()) {
//                                sink.next(SellResponse.builder()
//                                        .seller(user)
//                                        .countWasSold(sold)
//                                        .status(StatusSell.SELL_PART)
//                                        .build());
//                            } else {
//                                sink.next(SellResponse.builder()
//                                        .countWasSold(sold)
//                                        .seller(user)
//                                        .status(StatusSell.COMPLETED)
//                                        .build());
//                            }
//                        }
//                );
    }
}
