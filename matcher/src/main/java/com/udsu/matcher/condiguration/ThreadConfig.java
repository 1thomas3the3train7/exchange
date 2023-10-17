package com.udsu.matcher.condiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Executors;

@Configuration
public class ThreadConfig {
    @Bean
    public Scheduler virtual() {
        return Schedulers.fromExecutor(Executors.newVirtualThreadPerTaskExecutor());
    }
}
