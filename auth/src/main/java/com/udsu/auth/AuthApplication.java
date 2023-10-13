package com.udsu.auth;

import com.udsu.auth.model.Currency;
import com.udsu.auth.model.User;
import com.udsu.auth.model.buy.BuyRequest;
import com.udsu.auth.model.kafka.PublisherModel;
import com.udsu.auth.service.kafka.KafkaPublisher;
import com.udsu.auth.service.kafka.KafkaSubscriber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class AuthApplication implements CommandLineRunner {
	private final KafkaPublisher kafkaPublisher;
	private final KafkaSubscriber kafkaSubscriber;

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		PublisherModel<BuyRequest> stringPublisherModel = PublisherModel.<BuyRequest>builder()
				.id("Id").payload(BuyRequest.builder().id("myID").buyer(new User()).currency(Currency.RUB).count(10L).build()).build();

//		kafkaSubscriber.listenKafkaMessage("buy_order")
//				.map(s -> {
//					s.value();
//					log.info("MESSAGE FROM TOPIC: {}", s);
//					return s;
//				})
//				.subscribe(r -> {
//					System.out.println(r);
//				});


		kafkaPublisher.sendMessage(stringPublisherModel, "buy_order")
				.collectList()
				.subscribe(s -> {
					System.out.println(s.get(0));
				});
	}
}
