package com.udsu.trader.service.kafka;

import com.google.gson.Gson;
import com.udsu.trader.model.buy.BuyRequest;
import com.udsu.trader.model.entity.OperationHistory;
import com.udsu.trader.model.kafka.PublisherModel;
import com.udsu.trader.model.sell.SellRequest;
import com.udsu.trader.service.BuyOperationService;
import com.udsu.trader.service.SellOperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

import static com.udsu.trader.configuration.GsonConfig.publisherModelBuyRequest;
import static com.udsu.trader.configuration.GsonConfig.publisherModelSellRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService implements CommandLineRunner {
    private final KafkaPublisher kafkaPublisher;
    private final KafkaSubscriber kafkaSubscriber;
    private final BuyOperationService buyOperationService;
    private final SellOperationService sellOperationService;
    private final Gson gson;
    @Value("${kafka.sell-order.topic}")
    private String sellOrderTopicName;
    @Value("${kafka.buy-order.topic}")
    private String buyOrderTopicName;
    @Value("${kafka.operation_history.topic}")
    private String operationHistoryTopicName;

    @Override
    public void run(String... args) throws Exception {
        Runnable orderBuy = () -> {
            kafkaSubscriber.listenKafkaMessage(buyOrderTopicName)
                    .flatMap(rec -> {
                        PublisherModel<BuyRequest> publisherModel = gson.fromJson(rec.value(), publisherModelBuyRequest);
                        log.info("ORDER BUY TOPIC : {}", publisherModel);
                        return buyOperationService.buy(publisherModel.getPayload(), publisherModel.getPayload().getBuyer())
                                .subscribeOn(Schedulers.boundedElastic())
                                .collectList()
                                .flatMapMany(list -> kafkaPublisher.sendMessage(PublisherModel.<List<OperationHistory>>builder()
                                        .id(publisherModel.getId())
                                        .payload(list)
                                        .build(), operationHistoryTopicName))
                                .collectList()
                                .flatMap(list -> {
                                    log.info("COMMIT, LIST SEND RESULT : {}", list);
                                    return rec.receiverOffset().commit();
                                });
                    })
                    .onErrorResume(throwable -> {
                        log.error("ERROR LISTEN ORDER BUY TOPIC", throwable);
                        return Mono.empty();
                    })
                    .subscribe();
        };
        Thread orderBuyThread = new Thread(orderBuy);
        orderBuyThread.run();

        Runnable orderSell = () -> {
            kafkaSubscriber.listenKafkaMessage(sellOrderTopicName)
                    .flatMap(rec -> {
                        PublisherModel<SellRequest> publisherModel = gson.fromJson(rec.value(), publisherModelSellRequest);
                        log.info("ORDER SELL TOPIC : {}", publisherModel);
                        return sellOperationService.sell(publisherModel.getPayload(), publisherModel.getPayload().getSeller())
                                .subscribeOn(Schedulers.boundedElastic())
                                .collectList()
                                .flatMapMany(operationHistories -> kafkaPublisher.sendMessage(PublisherModel.<List<OperationHistory>>builder()
                                        .id(publisherModel.getId())
                                        .payload(operationHistories)
                                        .build(), operationHistoryTopicName))
                                .collectList()
                                .flatMap(senderResults -> {
                                    log.info("COMMINT, LIST SEND RESULT : {}", senderResults);
                                    return rec.receiverOffset().commit();
                                });
                    })
                    .onErrorResume(throwable -> {
                        log.info("ERROR LISTEN ORDER SELL TOPIC", throwable);
                        return Mono.empty();
                    })
                    .subscribe();
        };

        Thread orderSellThread = new Thread(orderSell);
        orderSellThread.run();
    }
}
