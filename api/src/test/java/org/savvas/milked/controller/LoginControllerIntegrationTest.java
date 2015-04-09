package org.savvas.milked.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.savvas.milked.Application;
import org.savvas.milked.controller.error.ErrorResponse;
import org.savvas.milked.controller.request.LoginRequest;
import org.savvas.milked.domain.MilkedUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;

import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.net.URI;

import static org.junit.Assert.*;
import static org.savvas.milked.controller.MilkedTestUtils.randomEmail;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port=0")

public class LoginControllerIntegrationTest {

    @Value("${local.server.port}")
    private int port;

    private final TestRestTemplate rest = new TestRestTemplate();

    @Test
    public void checkLoginReturns200ResponseCode(){
        //given
        String email = randomEmail();
        String baseUrl = "http://localhost:" + port;
        LoginRequest loginRequest = new LoginRequest(email, "pass");
        //when
        ResponseEntity<String> loggedInUserResponse = rest.postForEntity(URI.create(baseUrl +"/login"), loginRequest, String.class);
        //then
        assertEquals(200, loggedInUserResponse.getStatusCode().value());
   }
    @Test
    public void checkLoginReturnsErrorResponseCodeWhenInvalidPass(){
        //given
        String email = randomEmail();
        String baseUrl = "http://localhost:" + port;
        LoginRequest loginRequest = new LoginRequest(email, "");
        //when
        ResponseEntity<ErrorResponse> loggedInUserResponse = rest.postForEntity(URI.create(baseUrl +"/login"), loginRequest, ErrorResponse.class);
        //then
        assertEquals(400, loggedInUserResponse.getStatusCode().value());
        assertEquals("password", loggedInUserResponse.getBody().getErrors().get(0));
    }
    @Test
    public void checkLoginReturnsErrorResponseCodeWhenInvalidEmail(){
        //given
        String baseUrl = "http://localhost:" + port;
        LoginRequest loginRequest = new LoginRequest("asd@", "pass");
        //when
        ResponseEntity<ErrorResponse> loggedInUserResponse = rest.postForEntity(URI.create(baseUrl +"/login"), loginRequest, ErrorResponse.class);
        String userErrorMessage = loggedInUserResponse.getBody().getErrors().get(0);
        int userStatusCode = loggedInUserResponse.getStatusCode().value();
        //then
        assertEquals(400, userStatusCode);
        assertEquals("email", userErrorMessage);
    }
    @Test
    public void checkLoginReturnsErrorResponseWhenUserNotFound(){
        //given
        String baseUrl = "http://localhost:" + port;
        LoginRequest loginRequest = new LoginRequest("savvas123@ymail.com", "pass");
        //when
        ResponseEntity<ErrorResponse> loggedInUserResponse = rest.postForEntity(URI.create(baseUrl +"/login"), loginRequest, ErrorResponse.class);
        int userStatusCode = loggedInUserResponse.getStatusCode().value();
        //then
        assertEquals(400, userStatusCode);
    }
}