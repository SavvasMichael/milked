package org.savvas.shafted.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.savvas.shafted.Application;
import org.savvas.shafted.controller.error.ErrorResponse;
import org.savvas.shafted.controller.request.RegistrationRequest;
import org.savvas.shafted.domain.User;
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
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port=0")
public class RegistrationControllerIntegrationTest {

    @Value("${local.server.port}")
    private int port;

    private final TestRestTemplate rest = new TestRestTemplate();

    @Test
    public void registeringANewUserReturnsACreatedResponseCode() {
        // Given
        String registrationUrl = "http://localhost:" + port + "/registration";
        RegistrationRequest request = new RegistrationRequest("yoseph.samuel@gmail.com", "yoseph", "password");

        // When
        ResponseEntity<User> response = rest.postForEntity(URI.create(registrationUrl), request, User.class);

        // Then
        assertEquals("Unexpected response code.", 200, response.getStatusCode().value());
    }

    @Test
    public void invalidEmailReturnsBadRequest() {
        // Given
        String registrationUrl = "http://localhost:" + port + "/registration";
        RegistrationRequest request = new RegistrationRequest("yoseph.samuel", "yoseph", "password");

        // When
        ResponseEntity<ErrorResponse> response =  rest.postForEntity(URI.create(registrationUrl), request, ErrorResponse.class);

        // Then
        assertEquals("Unexpected response code.", 400, response.getStatusCode().value());
        assertEquals("Unexpected Error Message", "email", response.getBody().getErrors().get(0));
    }

    @Test
    public void emptyEmailReturnsBadRequest(){
        //given
        String registrationUrl = "http://localhost:" + port + "/registration";
        RegistrationRequest request = new RegistrationRequest("", "yoseph", "password");
        //when
        ResponseEntity<ErrorResponse> response =  rest.postForEntity(URI.create(registrationUrl), request, ErrorResponse.class);
        //then
        assertEquals("Unexpected response code.", 400, response.getStatusCode().value());
        assertEquals("Unexpected Error Message", "email", response.getBody().getErrors().get(0));

    }

    @Test
    public void missingEmailReturnsBadRequest(){
        //given
        String registrationUrl = "http://localhost:" + port + "/registration";
        RegistrationRequest request = new RegistrationRequest(null, "yoseph", "password");
        //when
        ResponseEntity<ErrorResponse> response =  rest.postForEntity(URI.create(registrationUrl), request, ErrorResponse.class);
        //then
        assertEquals("Unexpected response code.", 400, response.getStatusCode().value());
        assertEquals("Unexpected Error Message", "email", response.getBody().getErrors().get(0));
    }
}