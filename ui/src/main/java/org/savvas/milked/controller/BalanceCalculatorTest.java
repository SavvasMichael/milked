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
        MilkedUser milker2 = new MilkedUser(4);
        MilkedUser milkee2 = new MilkedUser(15);
        Integer txAmount = 100;
        Integer txAmount2 = 200;
        MilkingTransaction milkingTransaction = new MilkingTransaction(-1L, milker, milkee, txAmount, "");
        MilkingTransaction milkingTransaction2 = new MilkingTransaction(-2L, milker2, milkee2, txAmount2, "");
        MilkedUser[] milkedUsers = {milker, milkee, milker2, milkee2};
        MilkingTransaction[] milkingTransactions = {milkingTransaction, milkingTransaction2};

        // When
        balanceCalculator.calculateBalances(milkedUsers, milkingTransactions);

        // Then
        assertEquals("Unexpected milker balance.", (int) -txAmount, (int) milker.getBalance());
        assertEquals("Unexpected milker balance.", txAmount, milkee.getBalance());
        assertEquals("Unexpected milker balance.", (int) -txAmount2, (int) milker2.getBalance());
        assertEquals("Unexpected milker balance.", txAmount2, milkee2.getBalance());
    }

    @Test
    public void calculatesBalanceForMultipleTransactionsBetweenSameUsers() {
        //given
        BalanceCalculator balanceCalculator = new BalanceCalculator();
        MilkedUser txMilker = new MilkedUser(0);
        MilkedUser txMilkee = new MilkedUser(1);
        MilkedUser milker = new MilkedUser(0);
        MilkedUser milkee = new MilkedUser(1);

        MilkingTransaction milkingTransaction = new MilkingTransaction(-1L, txMilker, txMilkee, 50, "");
        MilkingTransaction milkingTransaction2 = new MilkingTransaction(-1L, txMilkee, txMilker, 20, "");
        MilkedUser[] milkedUsers = {milker, milkee};
        MilkingTransaction[] milkingTransactions = {milkingTransaction, milkingTransaction2};

        //when
        balanceCalculator.calculateBalances(milkedUsers, milkingTransactions);

        //then
        assertEquals("Unexpected Milker Balance", -30, (int) milker.getBalance());
        assertEquals("Unexpected Milkee Balance", 30, (int) milkee.getBalance());
    }
}