package com.udsu.updater.model.request;

import com.udsu.updater.model.Currency;
import com.udsu.updater.model.OperationStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OperationByDateRequest {
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private Currency fromCurrency;
    private Currency toCurrency;
    private OperationStatus operationStatus;
}
