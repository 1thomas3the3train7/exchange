package com.udsu.updater.model.entity;

import com.udsu.updater.model.Currency;
import com.udsu.updater.model.OperationStatus;
import com.udsu.updater.model.Order;
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

    public Order toOrder(Float price) {
        return Order.builder()
                .id(this.id)
                .currencyToBuy(this.currencyToBuy)
                .currencyToSell(this.currencyToSell)
                .countToBuy(this.countToBuy)
                .countToSell(this.countToSell)
                .price(price)
                .ownerId(this.ownerId)
                .fromBankAccount(this.fromBankAccount)
                .toBankAccount(this.toBankAccount)
                .datePublication(this.datePublication)
                .dateUpdate(this.dateUpdate)
                .active(this.active)
                .isenough(this.isenough)
                .operationStatus(OperationStatus.BUY)
                .build();
    }
}