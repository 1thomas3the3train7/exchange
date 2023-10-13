package com.udsu.trader.model.sell;



import com.udsu.trader.model.Currency;
import com.udsu.trader.model.User;
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
