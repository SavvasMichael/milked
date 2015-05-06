package org.savvas.milked.controller;

import org.springframework.web.client.RestTemplate;

public class ManualTesting {

    public static void main(String[] args) {

        RestTemplate rest = new RestTemplate();
        String baseUrl = "http://localhost:8080";
        MilkedTestUtils.givenTheUserIsRegisteredAndActivated(rest, baseUrl, "Savvas Michael", "pass");
        MilkedTestUtils.givenTheUserIsRegisteredAndActivated(rest, baseUrl, "Joe", "pass");
        MilkedTestUtils.givenTheMilkingGroup(rest, baseUrl, 1l, "Savvasgroup");
        MilkedTestUtils.givenTheUserHasJoinedTheGroup(rest, baseUrl, 2l, 1l);
    }
}
