package org.savvas.milked.controller;

public class GroupDetails {

    private MilkedUser[] milkedUsers;
    private MilkingTransaction[] milkingTransactions;
    private MilkedUser loggedInUser;

    public GroupDetails() {
    }

    public GroupDetails(MilkedUser[] milkedUsers, MilkingTransaction[] milkingTransactions, MilkedUser loggedInUser) {
        this.milkedUsers = milkedUsers;
        this.milkingTransactions = milkingTransactions;
        this.loggedInUser = loggedInUser;
    }

    public MilkedUser[] getMilkedUsers() {
        return milkedUsers;
    }

    public MilkingTransaction[] getMilkingTransactions() {
        return milkingTransactions;
    }

    public MilkedUser getLoggedInUser() {
        return loggedInUser;
    }
}
