package com.udsu.auth.service.kafka;

import com.google.gson.Gson;
import com.udsu.auth.model.kafka.PublisherModel;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderRecord;
import reactor.kafka.sender.SenderResult;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaPublisher {
    private Map<String, Object> pubConf = new HashMap<>();

    private SenderOptions<String, String> senderOptions;

    private KafkaSender<String, String> kafkaSender;
    private final Gson gson;

    @PostConstruct
    private void init() {
        pubConf.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        pubConf.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        pubConf.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        senderOptions = SenderOptions.<String, String>create(pubConf)
                .maxInFlight(1024);

        kafkaSender = KafkaSender.<String, String>create(senderOptions);
    }

    public Flux<SenderResult<String>> sendMessage(String key, String message, String topicName) {
        final String uuid = UUID.randomUUID().toString();
        final SenderRecord<String, String, String> valMessage = SenderRecord.create(topicName, 1, 3600L, key, message, uuid);
        return kafkaSender.send(Mono.just(valMessage))
                .doOnError(e -> log.info("Error send message topic name: " + topicName + " key: " + key + " message: " + message))
                .doOnNext(n -> log.info("Send topicName: " + topicName + ", message: " + message));
    }

    public Flux<SenderResult<String>> sendMessage(PublisherModel<?> publisherModel, String topicName) {
        final String uuid = UUID.randomUUID().toString();

        if (publisherModel.getDate() == null)
            publisherModel.setDate(LocalDateTime.now());

        final SenderRecord<String, String, String> valMessage = SenderRecord.create(topicName, 1, 3600L, publisherModel.getId(),
                gson.toJson(publisherModel) , uuid);

        return kafkaSender.send(Mono.just(valMessage))
                .doOnError(e -> log.info("Error send message {}, {} ", topicName, publisherModel ))
                .doOnNext(n -> log.info("Send topicName {}, {} ", topicName, publisherModel));
    }


}
