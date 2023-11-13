package com.udsu.auth.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BankAccount {
    private String id;
    private Currency currency;
    private Long owner_id;
    private Long balance;

    private User user;
}
