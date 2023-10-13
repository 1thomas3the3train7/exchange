package com.udsu.updater.model.kafka;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PublisherModel<T> {
    private String id;
    private T payload;
    private LocalDateTime date;
}
