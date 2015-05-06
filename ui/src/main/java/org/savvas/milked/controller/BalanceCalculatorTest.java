package org.savvas.milked.controller;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BalanceCalculatorTest {

    @Test
    public void calculatesBalanceForMilkedUser() {
        // Given
        BalanceCalculator balanceCalculator = new BalanceCalculator();
        MilkedUser milker = new MilkedUser(0);
        MilkedUser milkee = new MilkedUser(1);
        int txAmount = 100;
        MilkingTransaction milkingTransaction = new MilkingTransaction(-1L, milker, milkee, txAmount, "");
        MilkedUser[] milkedUsers = {milker, milkee};
        MilkingTransaction[] milkingTransactions = {milkingTransaction};

        // When
        balanceCalculator.calculateBalances(milkedUsers, milkingTransactions);

        // Then
        assertEquals("Unexpected milker balance.", -txAmount, milker.getBalance());
        assertEquals("Unexpected milker balance.", txAmount, milkee.getBalance());
    }
}