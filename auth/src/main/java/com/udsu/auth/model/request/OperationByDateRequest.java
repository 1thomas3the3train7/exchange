package com.udsu.auth.model.request;


import com.udsu.auth.model.Currency;
import com.udsu.auth.model.OperationStatus;
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
