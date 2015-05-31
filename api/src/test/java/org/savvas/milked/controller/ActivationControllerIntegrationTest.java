package org.savvas.milked.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.savvas.milked.MilkedApiApplication;
import org.savvas.milked.controller.error.ErrorResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.net.URI;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MilkedApiApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port=0")
public class ActivationControllerIntegrationTest {

    @Value("${local.server.port}")
    private int port;

    private final TestRestTemplate rest = new TestRestTemplate();

    @Test
    public void activationWithInvalidUuidReturns400() {
        //given
        String baseUrl = "http://localhost:" + port;
        String activationUrl = baseUrl + "/activation/";
        //when
        String invalidUuid = "D5G14q-afA3-dAgk2324";
        ResponseEntity<ErrorResponse> activationResponse = rest.getForEntity(URI.create(activationUrl + invalidUuid), ErrorResponse.class);
        //then
        ErrorResponse body = activationResponse.getBody();
        assertEquals("Unexpected error message", "Invalid UUID", body.getErrors().get(0));
    }

}