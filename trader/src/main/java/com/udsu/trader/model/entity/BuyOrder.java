package com.udsu.trader.model.entity;

import com.udsu.trader.model.Currency;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BuyOrder {
    private String id;
    private Currency currencyToBuy;
    private Currency currencyToSell;
    private Long countToBuy;
    private Long countToSell;
    private Long maxPrice;
    private Long ownerId;
    private Long fromBankAccount;
    private Long toBankAccount;
    private LocalDateTime datePublication;
    private LocalDateTime dateUpdate;
    private boolean active;
    private boolean isenough;
}
