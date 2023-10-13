package com.udsu.updater.service.websocket;

import org.jctools.maps.NonBlockingHashMap;
import io.netty.buffer.ByteBufAllocator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InfoWebSocketHandler implements WebSocketHandler {
    private final NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(ByteBufAllocator.DEFAULT);
    private final NonBlockingHashMap<String, WebSocketSession> sessionStorage;

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        final HttpHeaders headers = session.getHandshakeInfo().getHeaders();
        sessionStorage.put("key", session);
        return session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .then();
    }

    @Override
    public List<String> getSubProtocols() {
        return WebSocketHandler.super.getSubProtocols();
    }
}
