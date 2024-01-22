package com.paazl.util;

import com.paazl.data.CurrentBalance;

import java.math.BigInteger;

public class CurrentBalanceUtils {

    public static CurrentBalance addBalance(CurrentBalance balance, BigInteger amount) {
        CurrentBalance newBalance = new CurrentBalance();
        newBalance.setBalance(balance.getBalance().add(amount));
        return newBalance;
    }

    public static CurrentBalance subtractBalance(CurrentBalance balance, BigInteger amount) {
        CurrentBalance newBalance = new CurrentBalance();
        newBalance.setBalance(balance.getBalance().subtract(amount));
        return newBalance;
    }
}
