package org.savvas.milked.controller;

public class GroupDetails {

    private MilkedUser[] milkedUsers;
    private MilkingTransaction[] milkingTransactions;

    public GroupDetails() {
    }

    public GroupDetails(MilkedUser[] milkedUsers, MilkingTransaction[] milkingTransactions) {
        this.milkedUsers = milkedUsers;
        this.milkingTransactions = milkingTransactions;
    }

    public MilkedUser[] getMilkedUsers() {
        return milkedUsers;
    }

    public MilkingTransaction[] getMilkingTransactions() {
        return milkingTransactions;
    }
}
