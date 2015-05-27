package org.savvas.milked.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class GroupDetails {

    private MilkedUser[] milkedUsers;
    private List<MilkedUser> topThree = new ArrayList<>();
    private List<MilkedUser> bottomThree = new ArrayList<>();
    private MilkingTransaction[] milkingTransactions;
    private MilkedUser loggedInUser;

    public GroupDetails() {
    }

    public GroupDetails(MilkedUser[] milkedUsers, MilkingTransaction[] milkingTransactions, MilkedUser loggedInUser) {
        this.milkedUsers = milkedUsers;
        this.milkingTransactions = milkingTransactions;
        this.loggedInUser = loggedInUser;
        calculateLeaderBoard();
    }

    private void calculateLeaderBoard() {
        Arrays.sort(milkedUsers, new Comparator<MilkedUser>() {
            @Override
            public int compare(MilkedUser o1, MilkedUser o2) {
                return o1.getBalance().compareTo(o2.getBalance());
            }
        });
        for (int i = 0; i <= Math.min(3, milkedUsers.length - 1); i++) {
            bottomThree.add(milkedUsers[i]);
        }
        for (int i = milkedUsers.length - 1; i >= milkedUsers.length - Math.min(3, milkedUsers.length); i--) {
            topThree.add(milkedUsers[i]);
        }
    }

    public List<MilkedUser> getTopThree() {
        return topThree;
    }

    public List<MilkedUser> getBottomThree() {
        return bottomThree;
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

