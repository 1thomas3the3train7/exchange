package com.udsu.auth.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ControllerModel<T> {
    private T payload;
    private String error;
}
