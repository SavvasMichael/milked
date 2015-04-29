package org.savvas.milked.controller;

import org.springframework.web.client.RestTemplate;

public class ManualTesting {

    public static void main(String[] args) {

        RestTemplate rest = new RestTemplate();
        String baseUrl = "http://localhost:8080";
        MilkedTestUtils.givenTheUserIsRegisteredAndActivated(rest, baseUrl, "Savvas Michael", "pass");
    }
}
