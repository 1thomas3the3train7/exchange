package com.udsu.matcher.model.sell;


import com.udsu.matcher.model.Currency;
import com.udsu.matcher.model.entity.User;
import lombok.Data;

@Data
public class SellRequest {
    private String id;
    private User seller;
    private Currency currencyToSell;
    private Currency currencyToBuy;
    private String fromBankAccount;
    private String toBankAccount;
    private Long countToBuy;
    private Long countToSell;
    private Long minPrice;
}
