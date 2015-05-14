package org.savvas.milked.controller;

import org.savvas.milked.domain.MilkedUser;
import org.savvas.milked.domain.MilkingTransaction;
import org.springframework.web.client.RestTemplate;

public class ManualTesting {

    public static void main(String[] args) {

        RestTemplate rest = new RestTemplate();
        String baseUrl = "http://localhost:8080";
        MilkedUser user1 = MilkedTestUtils.givenTheUserIsRegisteredAndActivated(rest, baseUrl, "Savvas Michael", "pass");
        MilkedUser user2 = MilkedTestUtils.givenTheUserIsRegisteredAndActivated(rest, baseUrl, "Joe", "pass");
        MilkedTestUtils.givenTheMilkingGroup(rest, baseUrl, 1l, "Savvasgroup");
        MilkedTestUtils.givenTheUserHasJoinedTheGroup(rest, baseUrl, 2l, 1l);
        MilkingTransaction milkingTransaction = new MilkingTransaction(1l, user1, user2, 25, "Beef");
    }
}
