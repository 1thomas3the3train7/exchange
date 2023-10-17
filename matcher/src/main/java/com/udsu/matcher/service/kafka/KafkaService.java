package com.udsu.matcher.service.kafka;

import com.google.gson.Gson;
import com.udsu.matcher.model.buy.BuyRequest;
import com.udsu.matcher.model.kafka.PublisherModel;
import com.udsu.matcher.model.sell.SellRequest;
import com.udsu.matcher.service.match.impl.BuyServiceImpl;
import com.udsu.matcher.service.match.impl.SellServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import static com.udsu.matcher.condiguration.GsonConfig.publisherModelBuyRequest;
import static com.udsu.matcher.condiguration.GsonConfig.publisherModelSellRequest;


@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService implements CommandLineRunner {
    private final KafkaPublisher kafkaPublisher;
    private final KafkaSubscriber kafkaSubscriber;
    private final BuyServiceImpl buyService;
    private final SellServiceImpl sellService;
    private final Scheduler virtual;
    @Value("${kafka.buy.topic}")
    private String buyTopicName;
    @Value("${kafka.sell.topic}")
    private String sellTopicName;
    @Value("${kafka.sell-order.topic}")
    private String sellOrderTopicName;
    @Value("${kafka.buy-order.topic}")
    private String buyOrderTopicName;
    private final Gson gson;

    @Override
    public void run(String... args) throws Exception {
        Runnable taskBuy = () -> {
            kafkaSubscriber.listenKafkaMessage(buyTopicName)
                    .subscribeOn(virtual)
                    .map(rec -> {
                        final PublisherModel<BuyRequest> publisherModel = gson.fromJson(rec.value(), publisherModelBuyRequest);
                        log.info("BUY TOPIC : {}", publisherModel);
                        return publisherModel;
                    })
                    .flatMap(buyService::buyStart)
                    .subscribeOn(Schedulers.boundedElastic())
                    .flatMap(buyResponse -> kafkaPublisher.sendMessage(PublisherModel.<BuyRequest>builder()
                            .id(buyResponse.getId())
                            .payload(buyResponse.getRequest())
                            .build(), buyOrderTopicName))
                    .subscribeOn(virtual)
                    .onErrorResume(s -> {
                        log.error("ERROR", s);
                        return Mono.empty();
                    })
                    .subscribe();
        };
        Thread thread = new Thread(taskBuy);
        thread.start();

        Runnable taskSell = () -> {
            kafkaSubscriber.listenKafkaMessage(sellTopicName)
                    .subscribeOn(virtual)
                    .map(rec -> {
                        final PublisherModel<SellRequest> publisherModel = gson.fromJson(rec.value(), publisherModelSellRequest);
                        log.info("SELL TOPIC : {}", publisherModel);
                        return publisherModel;
                    })
                    .flatMap(sellService::sellStart)
                    .subscribeOn(Schedulers.boundedElastic())
                    .flatMap(sellResponse -> kafkaPublisher.sendMessage(PublisherModel.<SellRequest>builder()
                            .id(sellResponse.getId())
                            .payload(sellResponse.getRequest())
                            .build(), sellOrderTopicName))
                    .subscribeOn(virtual)
                    .onErrorResume(throwable -> {
                        log.error("ERROR IN LISTEN SELL TOPIC", throwable);
                        return Mono.empty();
                    })
                    .subscribe();
        };

        Thread sellThread = new Thread(taskSell);
        sellThread.start();
    }
}
