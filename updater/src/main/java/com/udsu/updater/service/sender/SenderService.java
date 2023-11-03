package com.udsu.updater.service.sender;

import com.google.gson.Gson;
import com.udsu.updater.model.OperationHistory;
import com.udsu.updater.model.OperationStatus;
import com.udsu.updater.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.jctools.maps.NonBlockingHashMap;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SenderService {
    private final OrderRepository orderRepository;
    private final NonBlockingHashMap<String, WebSocketSession> sessionStorage;
    private final NettyDataBufferFactory nettyDataBufferFactory;
    private final Gson gson;

    public Mono<Void> send(List<OperationHistory> listsHistory) {
        return Flux.fromIterable(listsHistory)
                .flatMap(operationHistory ->
                        operationHistory.getOperationStatus() == OperationStatus.BUY ?
                                orderRepository.findBuyOrderById(operationHistory.getId())
                                        .map(buyOrder -> buyOrder.toOrder(operationHistory.getPrice())) :
                                orderRepository.findSellOrderById(operationHistory.getId())
                                        .map(sellOrder -> sellOrder.toOrder(operationHistory.getPrice())))
                .collectList()
                .flatMapMany(list -> {
                    var values = sessionStorage.values();
                    return Flux.fromIterable(values)
                            .flatMap(session -> session.send(Mono.just(
                                    new WebSocketMessage(WebSocketMessage.Type.TEXT,
                                            nettyDataBufferFactory.wrap(gson.toJson(list).getBytes())))));
                })
                .then();
    }
}
