package com.udsu.auth.model.request;


import com.udsu.auth.model.Currency;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BankAccountRequest {
    private Currency currency;
    private Long userId;
}
