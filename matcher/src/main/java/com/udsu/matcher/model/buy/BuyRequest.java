package com.udsu.matcher.model.buy;


import com.udsu.matcher.model.Currency;
import com.udsu.matcher.model.entity.User;
import lombok.Data;

@Data
public class BuyRequest {
    private String id;
    private User buyer;
    private Currency currencyToBuy;
    private Currency currencyToSell;
    private String fromBankAccount;
    private String toBankAccount;
    private Long countToBuy;
    private Long countToSell;
    private Long maxPrice;
}
