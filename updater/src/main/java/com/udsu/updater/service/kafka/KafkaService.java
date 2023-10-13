package com.udsu.updater.service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService implements CommandLineRunner {
    private final KafkaSubscriber kafkaSubscriber;
    @Value("${kafka.operation_history.topic}")
    private String operationHistoryTopicName;

    @Override
    public void run(String... args) throws Exception {
        Runnable taskSendInfo = () -> {
            kafkaSubscriber.listenKafkaMessage(operationHistoryTopicName)
                    .publishOn(Schedulers.boundedElastic())
                    .subscribe();
        };
    }
}
