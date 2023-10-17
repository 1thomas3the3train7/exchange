package com.udsu.matcher.service.match.impl;

import com.udsu.matcher.exception.NotMatchingUserBankAccount;
import com.udsu.matcher.model.entity.User;
import com.udsu.matcher.model.kafka.PublisherModel;
import com.udsu.matcher.model.sell.SellRequest;
import com.udsu.matcher.model.sell.SellResponse;
import com.udsu.matcher.model.sell.StatusSell;
import com.udsu.matcher.repository.impl.SellOperationRepository;
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
public class SellServiceImpl {
    private final SellOperationRepository sellOperationRepository;
    private final UserRepository userRepository;

    public Mono<SellResponse> createSellOperation(SellRequest sellRequest, User user) {
        return sellOperationRepository.lockBalance(sellRequest)
                .flatMap(bankAccount -> sellOperationRepository.createOrderSell(sellRequest, user))
                .map(id -> SellResponse.builder().id(id.toString()).request(sellRequest).seller(user).status(StatusSell.SEND_OPERATION).build());
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public Mono<SellResponse> sellStart(PublisherModel<SellRequest> publisherModel) {
        final SellRequest sellRequest = publisherModel.getPayload();
        return userRepository.findUserAndBankAccountByTokenAndAccountsIdAndCurrency(sellRequest.getSeller(),
                        sellRequest.getToBankAccount(),
                        sellRequest.getFromBankAccount(),
                        sellRequest.getCurrencyToBuy(),
                        sellRequest.getCurrencyToSell()
                ).<User>handle((user, sink) -> {
                    if (BankAccountUtils.userIsContainsBankAccounts(user, sellRequest)) {
                        sink.next(user);
                        return;
                    }
                    log.error("NOT MATCH USER WITH ACCOUNT, {}, {}", publisherModel, user);
                    sink.error(new NotMatchingUserBankAccount("NOT MATCH USER WITH ACCOUNT"));
                })
                .flatMap(user -> createSellOperation(sellRequest, user));
    }
}
