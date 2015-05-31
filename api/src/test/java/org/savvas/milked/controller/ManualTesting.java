package org.savvas.milked.controller;

import org.savvas.milked.domain.MilkedUser;
import org.savvas.milked.domain.MilkingTransaction;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;

public class ManualTesting {

    public static void main(String[] args) throws UnsupportedEncodingException {

        RestTemplate rest = new RestTemplate();
        String baseUrl = "http://api.milked.io";
        MilkedUser user1 = MilkedTestUtils.givenTheUserIsRegisteredAndActivated(rest, baseUrl, "Savvas Michael", "pass");
        MilkedUser user2 = MilkedTestUtils.givenTheUserIsRegisteredAndActivated(rest, baseUrl, "Joe", "pass");
        MilkedTestUtils.givenTheMilkingGroup(rest, baseUrl, 1l, "Savvasgroup");
        MilkedTestUtils.givenTheUserHasJoinedTheGroup(rest, baseUrl, "asd@asdas.sadsa", 1l);
        MilkingTransaction milkingTransaction = new MilkingTransaction(1l, user1, user2, 25, "Beef");
    }
}
