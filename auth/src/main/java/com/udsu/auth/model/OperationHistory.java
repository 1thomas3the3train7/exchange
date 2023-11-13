package com.udsu.auth.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OperationHistory {
    private Long id;
    private Float countInOperation;
    private OperationStatus operationStatus;
    private Long toBankAccountId;
    private Long orderId;
    private Float price;
    private LocalDateTime dateCreation;
}
