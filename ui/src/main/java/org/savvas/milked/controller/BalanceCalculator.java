package org.savvas.milked.controller;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BalanceCalculator {
    public static void calculateBalances(MilkedUser[] milkedUsers, MilkingTransaction[] milkingTransactions) {
        Map<Long, MilkedUser> userMap = new HashMap<>();

        for (MilkedUser user : milkedUsers) {
            userMap.put(user.getId(), user);
        }

        for (MilkingTransaction tx : milkingTransactions) {
            MilkedUser txMilker = tx.getMilker();
            MilkedUser txMilkee = tx.getMilkee();

            int milkerBalance = txMilker.getBalance() - tx.getAmount();
            int milkeeBalance = txMilkee.getBalance() + tx.getAmount();

            userMap.get(txMilker.getId()).setBalance(milkerBalance);
            userMap.get(txMilkee.getId()).setBalance(milkeeBalance);
        }
    }
}
