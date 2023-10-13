package com.udsu.matcher.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TestRepo {
    private final DatabaseClient databaseClient;
    private final DatabaseClient databaseClient1;

    public Mono<String> test() {
        return databaseClient.sql("SELECT * FROM users")
                .map(s -> {
//                    System.out.println(s);
                    return s.toString();
                })
                .all()
                .collectList()
                .map(s -> s.toString());
    }

    public Mono<String> test1() {
        System.out.println("W1");
        return databaseClient.sql("SELECT * FROM status_load_trademarks LIMIT 10")
                .map(s -> {
                    log.info(s.toString());
                    return s;
                })
                .all()
                .collectList()
                .map(l -> l.toString());
    }
}
