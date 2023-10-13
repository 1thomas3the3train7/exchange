package com.udsu.matcher.service.kafka;

import com.google.gson.Gson;
import com.udsu.matcher.model.buy.BuyRequest;
import com.udsu.matcher.model.kafka.PublisherModel;
import com.udsu.matcher.model.kafka.SubscriberModel;
import com.udsu.matcher.model.sell.SellRequest;
import com.udsu.matcher.service.match.impl.BuyServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
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
    @Value("${kafka.buy.topic}")
    private String buyTopicName;
    @Value("${kafka.sell.topic}")
    private String sellTopicName;
    private final Gson gson;

    @Override
    public void run(String... args) throws Exception {
        Runnable taskBuy = () -> {
            kafkaSubscriber.listenKafkaMessage(buyTopicName)
                    .publishOn(Schedulers.boundedElastic())
                    .map(rec -> {
                        final PublisherModel<BuyRequest> publisherModel = gson.fromJson(rec.value(), publisherModelBuyRequest);
                        log.info("BUY TOPIC : {}", publisherModel);
                        return publisherModel;
                    })
                    .flatMap(buyService::buyStart)
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
                    .publishOn(Schedulers.boundedElastic())
                    .map(rec -> {
                        final PublisherModel<SellRequest> publisherModel = gson.fromJson(rec.value(), publisherModelSellRequest);
                        log.info("SELL TOPIC : {}", publisherModel);
                        return publisherModel;
                    })

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
