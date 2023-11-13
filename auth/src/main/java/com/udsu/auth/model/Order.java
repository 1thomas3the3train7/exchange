package com.udsu.auth.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Order {
    private String id;
    private Currency currencyToBuy;
    private Currency currencyToSell;
    private Long countToBuy;
    private Long countToSell;
    private Float price;
    private Long ownerId;
    private Long fromBankAccount;
    private Long toBankAccount;
    private LocalDateTime datePublication;
    private LocalDateTime dateUpdate;
    private Boolean active;
    private Boolean isenough;
    private OperationStatus operationStatus;
}
