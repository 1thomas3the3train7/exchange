package com.udsu.auth.model.buy;

import com.udsu.auth.model.Currency;
import com.udsu.auth.model.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BuyRequest {
    private String id;
    private User buyer;
    private Currency currencyToBuy;
    private Currency currencyToSell;
    private String fromBankAccount;
    private String toBankAccount;
    private Long countToBuy;
    private Long maxPrice;
}
