package com.udsu.auth.utils;

import com.google.gson.reflect.TypeToken;
import com.udsu.auth.model.BankAccount;
import com.udsu.auth.model.OperationHistory;
import com.udsu.auth.model.Order;
import com.udsu.auth.model.rsocket.RSocketModel;

import java.lang.reflect.Type;
import java.util.List;

public class GsonUtils {
    public static final Type rSocketModelListOrder = new TypeToken<RSocketModel<List<Order>>>(){}.getType();
    public static final Type rSocketModelOperationHistory = new TypeToken<RSocketModel<OperationHistory>>(){}.getType();

    public static final Type rSocketModelBankAccount = new TypeToken<RSocketModel<BankAccount>>(){}.getType();

    public static final Type rSocketModelListBankAccount = new TypeToken<RSocketModel<List<BankAccount>>>(){}.getType();
}
