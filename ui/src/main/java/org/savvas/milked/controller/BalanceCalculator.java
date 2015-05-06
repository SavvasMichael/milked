package org.savvas.milked.controller;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BalanceCalculator {
    public void calculateBalances(MilkedUser[] milkedUsers, MilkingTransaction[] milkingTransactions) {

        Map userIdMap = new HashMap<>();
        Map transactionMap = new HashMap<>();

        for (MilkingTransaction milkingTransaction : milkingTransactions) {
            transactionMap.put("milkerId", milkingTransaction.getMilker().getId());
            transactionMap.put("milkeeId", milkingTransaction.getMilkee().getId());
            transactionMap.put("amount", milkingTransaction.getAmount());
        }
        for (MilkedUser milkedUser : milkedUsers) {
            userIdMap.put("userId", milkedUser.getId());
        }

    }
}
