package com.udsu.updater.model.request;

import com.udsu.updater.model.Currency;
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
