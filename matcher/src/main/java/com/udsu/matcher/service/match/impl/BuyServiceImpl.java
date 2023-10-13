package com.udsu.matcher.service.match.impl;

import com.udsu.matcher.exception.NotMatchingUserBankAccount;
import com.udsu.matcher.model.buy.BuyRequest;
import com.udsu.matcher.model.buy.BuyResponse;
import com.udsu.matcher.model.buy.StatusBuy;
import com.udsu.matcher.model.entity.User;
import com.udsu.matcher.model.kafka.PublisherModel;
import com.udsu.matcher.repository.impl.BuyOperationRepository;
import com.udsu.matcher.repository.impl.UserRepository;
import com.udsu.matcher.utils.BankAccountUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class BuyServiceImpl {
    private final BuyOperationRepository buyOperationRepository;
    private final UserRepository userRepository;

    public Mono<BuyResponse> createBuyOperation(BuyRequest buyRequest, User user) {
        return buyOperationRepository
                .lockBalance(buyRequest)
                .flatMap(bankAccount -> buyOperationRepository.createOrderBuy(buyRequest, user))
                .map(id -> BuyResponse.builder().id(id.toString()).status(StatusBuy.SEND_OPERATION).buyer(user).build());
    }
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public Mono<BuyResponse> buyStart(PublisherModel<BuyRequest> publisherModel) {
        final BuyRequest buyRequest = publisherModel.getPayload();
        return userRepository.findUserAndBankAccountByTokenAndAccountsIdAndCurrency(
                        buyRequest.getBuyer(),
                        buyRequest.getFromBankAccount(),
                        buyRequest.getToBankAccount(),
                        buyRequest.getCurrencyToBuy(),
                        buyRequest.getCurrencyToSell())
                .<User>handle((user, sink) -> {
                    if (BankAccountUtils.userIsContainsBankAccounts(user, buyRequest)) {
                        sink.next(user);
                        return;
                    }
                    log.error("NOT MATCH USER WITH ACCOUNT, {}, {}", publisherModel, user);
                    sink.error(new NotMatchingUserBankAccount("NOT MATCH USER WITH ACCOUNT"));
                })
                .flatMap(user -> createBuyOperation(buyRequest, user));
    }
}
