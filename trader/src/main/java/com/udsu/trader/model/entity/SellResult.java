package com.udsu.trader.model.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SellResult {
    private Long id;
    private Long idOrderBuy;
    private Long idBankAccount;
    private Float operationSum;
    private Float price;
    private LocalDateTime dateCreation;
    private boolean isFinalResult;
    private boolean isEnough;
}
