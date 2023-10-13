package com.udsu.trader.model.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SellResult {
    private Long id;
    private Long idOrderBuy;
    private Long idBankAccount;
    private Float operationSum;
    private boolean isFinalResult;
    private boolean isEnough;
}
