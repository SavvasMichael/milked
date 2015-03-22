package org.savvas.shafted.controller;

import org.hibernate.validator.constraints.Email;
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
import java.util.List;

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
        String baseUrl = "http://localhost:" + port;
        String registrationUrl = baseUrl + "/registration";
        RegistrationRequest request = new RegistrationRequest("michaelsavvas@ymail.com", "savvas", "password");
        // When
        ResponseEntity<String> registrationResponse = rest.postForEntity(URI.create(registrationUrl), request, String.class);
        String userPath = registrationResponse.getHeaders().getFirst("Location");
        ResponseEntity<User> userResponse = rest.getForEntity(URI.create(baseUrl + userPath), User.class);
        // Then
        assertEquals("Unexpected response code.", 201, registrationResponse.getStatusCode().value());
        assertEquals("Unexpected Location Header.", "/user/1", userPath);
        User user = userResponse.getBody();
        assertEquals("michaelsavvas@ymail.com", user.getEmail());
        assertEquals("savvas", user.getName());
        assertEquals("password", user.getPassword());
    }

    @Test
    public void invalidEmailReturnsBadRequest() {
        // Given
        String registrationUrl = "http://localhost:" + port + "/registration";
        RegistrationRequest request = new RegistrationRequest("michaelsavvas", "savvas", "password");

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
        RegistrationRequest request = new RegistrationRequest("", "savvas", "password");
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
        RegistrationRequest request = new RegistrationRequest(null, "savvas", "password");
        //when
        ResponseEntity<ErrorResponse> response =  rest.postForEntity(URI.create(registrationUrl), request, ErrorResponse.class);
        //then
        assertEquals("Unexpected response code.", 400, response.getStatusCode().value());
        assertEquals("Unexpected Error Message", "email", response.getBody().getErrors().get(0));
    }

    @Test
    public void missingNameReturnsBadRequest() {
        //given
        String registrationUrl = "http://localhost:" + port + "/registration";
        RegistrationRequest request = new RegistrationRequest("michaelsavvas@ymail.com", null, "password");
        //when
        ResponseEntity<ErrorResponse> response = rest.postForEntity(URI.create(registrationUrl), request, ErrorResponse.class);
        //then
        assertEquals("Unexpected response code.", 400, response.getStatusCode().value());
        assertEquals("Unexpected Error Message", "name", response.getBody().getErrors().get(0));
    }

    @Test
    public void emptyNameReturnsBadRequest() {
        //given
        String registrationUrl = "http://localhost:" + port + "/registration";
        RegistrationRequest request = new RegistrationRequest("michaelsavvas@ymail.com", "", "password");
        //when
        ResponseEntity<ErrorResponse> response = rest.postForEntity(URI.create(registrationUrl), request, ErrorResponse.class);
        //then
        assertEquals("Unexpected response code.", 400, response.getStatusCode().value());
        assertEquals("Unexpected Error Message", "name", response.getBody().getErrors().get(0));
    }

    @Test
    public void emptyPasswordReturnsBadRequest() {
        //given
        String registrationUrl = "http://localhost:" + port + "/registration";
        RegistrationRequest request = new RegistrationRequest("michaelsavvas@ymail.com", "Savvas", null);
        //when
        ResponseEntity<ErrorResponse> response = rest.postForEntity(URI.create(registrationUrl), request, ErrorResponse.class);
        //then
        assertEquals("Unexpected response code.", 400, response.getStatusCode().value());
        assertEquals("Unexpected Error Message", "password", response.getBody().getErrors().get(0));
    }

    @Test
    public void missingPasswordReturnsBadRequest() {
        //given
        String registrationUrl = "http://localhost:" + port + "/registration";
        RegistrationRequest request = new RegistrationRequest("michaelsavvas@ymail.com", "Savvas", "");
        //when
        ResponseEntity<ErrorResponse> response = rest.postForEntity(URI.create(registrationUrl), request, ErrorResponse.class);
        //then
        assertEquals("Unexpected response code.", 400, response.getStatusCode().value());
        assertEquals("Unexpected Error Message", "password", response.getBody().getErrors().get(0));
    }

    @Test
    public void registeringUserWithDuplicateEmailReturnsBadRequest() {
        //given
        String registrationUrl = "http://localhost:" + port + "/registration";
        RegistrationRequest request = new RegistrationRequest("michaelsavvas@ymail.com", "Savvas", "pass");
        RegistrationRequest request2 = new RegistrationRequest("michaelsavvas@ymail.com", "Savvas", "pass");
        //when
        ResponseEntity<ErrorResponse> response = rest.postForEntity(URI.create(registrationUrl), request, ErrorResponse.class);
        ResponseEntity<ErrorResponse> response2 = rest.postForEntity(URI.create(registrationUrl), request2, ErrorResponse.class);

        //then
        assertEquals(201, response.getStatusCode().value());
        assertEquals("This Email has already been used", 400, response2.getStatusCode().value());
        assertEquals("Unexpected error message", "This email has already been registered", response2.getBody().getErrors().get(0));
    }
}