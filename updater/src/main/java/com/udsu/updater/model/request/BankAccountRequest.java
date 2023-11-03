package com.udsu.updater.model.request;

import com.udsu.updater.model.Currency;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BankAccountRequest {
    private Currency currency;
    private Long userId;
}
