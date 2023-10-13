package com.udsu.matcher.utils;

import com.udsu.matcher.model.buy.BuyRequest;
import com.udsu.matcher.model.entity.BankAccount;
import com.udsu.matcher.model.entity.User;
import com.udsu.matcher.model.sell.SellRequest;
import org.apache.commons.lang3.StringUtils;

public class BankAccountUtils {
    public static boolean userIsContainsBankAccounts(User user, BuyRequest buyRequest) {
        boolean toBuyAccountContains = false;
        boolean toSellAccountContains = false;
        for (int i = 0; i < user.getBankAccounts().size(); i++) {
            final BankAccount curr = user.getBankAccounts().get(i);
            if (StringUtils.equalsIgnoreCase(curr.getId(), buyRequest.getFromBankAccount())
                    && buyRequest.getCurrencyToBuy() == curr.getCurrency())
                toBuyAccountContains = true;
            if (StringUtils.equalsIgnoreCase(curr.getId(), buyRequest.getToBankAccount())
                    && buyRequest.getCurrencyToSell() == curr.getCurrency())
                toSellAccountContains = true;
        }
        return toBuyAccountContains && toSellAccountContains;
    }

    public static boolean userIsContainsBankAccounts(User user, SellRequest sellRequest) {
        boolean toBuyAccountContains = false;
        boolean toSellAccountContains = false;
        for (int i = 0; i < user.getBankAccounts().size(); i++) {
            final BankAccount curr = user.getBankAccounts().get(i);
            if (StringUtils.equalsIgnoreCase(curr.getId(), sellRequest.getToBankAccount())
                    && sellRequest.getCurrencyToBuy() == curr.getCurrency())
                toBuyAccountContains = true;
            if (StringUtils.equalsIgnoreCase(curr.getId(), sellRequest.getFromBankAccount())
                    && sellRequest.getCurrencyToSell() == curr.getCurrency())
                toSellAccountContains = true;
        }
        return toBuyAccountContains && toSellAccountContains;
    }
}
