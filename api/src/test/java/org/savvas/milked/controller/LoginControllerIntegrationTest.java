package org.savvas.milked.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.savvas.milked.Application;
import org.savvas.milked.controller.error.ErrorResponse;
import org.savvas.milked.controller.request.LoginRequest;
import org.savvas.milked.controller.request.RegistrationRequest;
import org.savvas.milked.domain.MilkedUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.net.URI;

import static org.junit.Assert.*;
import static org.savvas.milked.controller.MilkedTestUtils.givenTheUserIsRegisteredAndActivated;
import static org.savvas.milked.controller.MilkedTestUtils.randomEmail;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port=0")

public class LoginControllerIntegrationTest {

    @Value("${local.server.port}")
    private int port;

    private final TestRestTemplate rest = new TestRestTemplate();
    private String baseUrl;

    @Before
    public void before() {
        baseUrl = "http://localhost:" + port;
    }

    @Test
    public void checkLoginReturns200ResponseCode(){
        //given
        MilkedUser milkedUser = givenTheUserIsRegisteredAndActivated(rest, baseUrl, "savvas", "password");

        //when
        LoginRequest loginRequest = new LoginRequest(milkedUser.getEmail(), "password");
        ResponseEntity<MilkedUser> loggedInUserResponse = rest.postForEntity(URI.create(baseUrl + "/login"), loginRequest, MilkedUser.class);
        MilkedUser loggedInUser = loggedInUserResponse.getBody();

        //then
        assertEquals(200, loggedInUserResponse.getStatusCode().value());
        assertTrue("Only activated users can login.", loggedInUser.isActivated());
        assertEquals("Logged in user's email does not match login requested.", milkedUser.getEmail(), loggedInUser.getEmail());
   }

    //TODO: add a test to check unactivated users cannot login

    @Test
    public void checkLoginReturnsErrorResponseCodeWhenInvalidEmail(){
        //given
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
        LoginRequest loginRequest = new LoginRequest(randomEmail(), "pass");
        //when
        givenTheUserIsRegisteredAndActivated(rest, baseUrl, "savvas", "pass");
        ResponseEntity<ErrorResponse> pendingUserLogin = rest.postForEntity(URI.create(baseUrl + "/login"), loginRequest, ErrorResponse.class);
        int userStatusCode = pendingUserLogin.getStatusCode().value();
        //then
        assertEquals(401, userStatusCode);
        assertEquals("Email does not match our records", pendingUserLogin.getBody().getErrors().get(0));
    }

    @Test
    public void checkLoginReturnsErrorResponseWhenPassDoesNotMatch() {
        //given
        MilkedUser user = givenTheUserIsRegisteredAndActivated(rest, baseUrl, "savvas", "password");
        String email = user.getEmail();
        //when
        LoginRequest loginRequest = new LoginRequest(email, "pass");
        ResponseEntity<ErrorResponse> pendingUserLogin = rest.exchange(URI.create(baseUrl + "/login"), HttpMethod.POST, new HttpEntity<LoginRequest>(loginRequest), ErrorResponse.class);
        //then
        int userStatusCode = pendingUserLogin.getStatusCode().value();
        assertEquals("Password does not match", pendingUserLogin.getBody().getErrors().get(0));
        assertEquals(401, userStatusCode);
    }

    @Test
    public void checkLoginReturns200WithMultipleRegistrations() {
        //given
        RegistrationRequest user1RegistrationRequest = new RegistrationRequest("savvas123@ymail.com", "savvas", "password1");
        RegistrationRequest user2RegistrationRequest = new RegistrationRequest("savvas1234@ymail.com", "savvas", "password2");
        RegistrationRequest user3RegistrationRequest = new RegistrationRequest("savvas1235@ymail.com", "savvas", "password3");
        LoginRequest loginRequest = new LoginRequest("savvas1234@ymail.com", "password2");
        //when
        rest.postForEntity(URI.create(baseUrl + "/registration"), user1RegistrationRequest, String.class);
        rest.postForEntity(URI.create(baseUrl + "/registration"), user2RegistrationRequest, String.class);
        rest.postForEntity(URI.create(baseUrl + "/registration"), user3RegistrationRequest, String.class);
        ResponseEntity<String> pendingUserLogin = rest.postForEntity(URI.create(baseUrl + "/login"), loginRequest, String.class);
        int userStatusCode = pendingUserLogin.getStatusCode().value();
        //then
        assertEquals(200, userStatusCode);
    }

    @Test
    public void checkThatOnlyAnActivatedUserCanLogin() {
        //given
        MilkedUser registeredAndActivatedUser = givenTheUserIsRegisteredAndActivated(rest, baseUrl, "savvas", "password1");
        LoginRequest loginRequest = new LoginRequest(registeredAndActivatedUser.getEmail(), "password1");
        //when
        ResponseEntity<MilkedUser> pendingUserLogin = rest.postForEntity(URI.create(baseUrl + "/login"), loginRequest, MilkedUser.class);
        MilkedUser registeredUser = pendingUserLogin.getBody();
        //then
        assertEquals(200, pendingUserLogin.getStatusCode().value());
        assertTrue("Only activated Users can Login", registeredUser.isActivated());
    }
}