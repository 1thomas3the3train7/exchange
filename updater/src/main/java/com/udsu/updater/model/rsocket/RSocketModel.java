package com.udsu.updater.model.rsocket;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RSocketModel<T> {
    private T payload;
    private String error;
}
