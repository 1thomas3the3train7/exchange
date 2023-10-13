package com.udsu.trader.model.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BuyResult {
    private Long id;
    private Long idOrderSell;
    private Long idBankAccount;
    private Float operationSum;
    private LocalDateTime dateCreation;
    private Float price;
    private boolean isFinalResult;
    private boolean isEnough;
}
