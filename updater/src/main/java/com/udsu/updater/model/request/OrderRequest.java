package com.udsu.updater.model.request;

import com.udsu.updater.model.Currency;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderRequest {
    private Currency currencyToBuy;
    private Currency currencyToSell;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
}
