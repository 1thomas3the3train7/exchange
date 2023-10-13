package com.udsu.matcher.service.kafka;

import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KafkaSubscriber {
    private final Map<String, Object> subProp = new HashMap<>();

    @PostConstruct
    private void init() {
        subProp.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        subProp.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        subProp.put(ConsumerConfig.GROUP_ID_CONFIG, "message-group-id1");
        subProp.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    }

    public Flux<ReceiverRecord<String, String>> listenKafkaMessage(String topicName) {
        final ReceiverOptions<String, String> receiverOptions = ReceiverOptions.<String, String>create(subProp)
                .subscription(List.of(topicName));

        return KafkaReceiver.create(receiverOptions)
                .receive();
    }

}
