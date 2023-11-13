package com.udsu.auth.model.request;


import com.udsu.auth.model.Currency;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LastPriceRequest {
    private Currency currencyToBuy;
    private Currency currencyToSell;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
}
