package com.udsu.auth.service.kafka;

import com.google.gson.Gson;
import com.udsu.auth.model.buy.BuyRequest;
import com.udsu.auth.model.buy.BuyResponse;
import com.udsu.auth.model.buy.StatusBuy;
import com.udsu.auth.model.kafka.PublisherModel;
import com.udsu.auth.model.sell.SellRequest;
import com.udsu.auth.model.sell.SellResponse;
import com.udsu.auth.model.sell.StatusSell;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService {
    private final KafkaPublisher kafkaPublisher;
    private final KafkaSubscriber kafkaSubscriber;
    @Value("${kafka.buy.topic}")
    private String buyTopicName;
    @Value("${kafka.sell.topic}")
    private String sellTopicName;
    private final Gson gson;

    public Mono<BuyResponse> sendToBuyTopic(BuyRequest buyRequest) {
        return kafkaPublisher.sendMessage(PublisherModel.fromBuyRequest(buyRequest), buyTopicName)
                .collectList()
                .map(stringSenderResult -> {
                    if (stringSenderResult.isEmpty())
                        return BuyResponse.fromBuyRequest(buyRequest, StatusBuy.ERROR);
                    if (stringSenderResult.get(0).exception() == null)
                        return BuyResponse.fromBuyRequest(buyRequest, StatusBuy.SEND_OPERATION);
                    return BuyResponse.fromBuyRequest(buyRequest, StatusBuy.ERROR);
                });
    }

    public Mono<SellResponse> sendToSellTopic(SellRequest sellRequest) {
        return kafkaPublisher.sendMessage(PublisherModel.fromSellRequest(sellRequest), sellTopicName)
                .collectList()
                .map(senderResults -> {
                    if (senderResults.isEmpty())
                        return SellResponse.fromSellRequest(sellRequest, StatusSell.ERROR);
                    if (senderResults.get(0).exception() == null)
                        return SellResponse.fromSellRequest(sellRequest, StatusSell.SEND_OPERATION);
                    return SellResponse.fromSellRequest(sellRequest, StatusSell.ERROR);
                });
    }
}
