package com.udsu.auth.model.sell;

import com.udsu.auth.model.Currency;
import com.udsu.auth.model.User;
import lombok.Data;

@Data
public class SellRequest {
    private String id;
    private User seller;
    private Currency currencyToSell;
    private Currency currencyToBuy;
    private String fromBankAccount;
    private String toBankAccount;
    private Long count;
    private Long minPrice;
}
