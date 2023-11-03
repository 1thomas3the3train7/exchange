package com.udsu.updater.model.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class User {
    private Long id;
    private String token;
    private List<BankAccount> bankAccounts;
}
