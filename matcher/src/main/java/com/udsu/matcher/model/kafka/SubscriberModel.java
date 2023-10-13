package com.udsu.matcher.model.kafka;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SubscriberModel<T> {
    private String id;
    private T payload;
    private LocalDateTime date;
}
