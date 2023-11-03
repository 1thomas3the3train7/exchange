package com.udsu.updater.service.kafka;

import com.google.gson.Gson;
import com.udsu.updater.model.OperationHistory;
import com.udsu.updater.model.kafka.PublisherModel;
import com.udsu.updater.service.sender.SenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import reactor.core.scheduler.Schedulers;

import java.util.List;

import static com.udsu.updater.configuration.GsonConfig.publisherModelListOperationHistory;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService implements CommandLineRunner {
    private final KafkaSubscriber kafkaSubscriber;
    @Value("${kafka.operation_history.topic}")
    private String operationHistoryTopicName;
    private final SenderService senderService;
    private final Gson gson;

    @Override
    public void run(String... args) throws Exception {
        Runnable taskSendInfo = () -> {
            kafkaSubscriber.listenKafkaMessage(operationHistoryTopicName)
                    .subscribeOn(Schedulers.boundedElastic())
                    .map(rec -> {
                        PublisherModel<List<OperationHistory>> publisherModel = gson.fromJson(rec.value(), publisherModelListOperationHistory);
                        log.info("ORDER HISTORY TOPIC : {}", publisherModel);
                        return publisherModel.getPayload();
                    })
                    .flatMap(senderService::send)
                    .subscribe();
        };

        Thread taskSendThread = new Thread(taskSendInfo);
        taskSendThread.run();
    }
}
