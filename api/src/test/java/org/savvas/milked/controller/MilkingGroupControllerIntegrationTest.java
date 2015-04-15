package org.savvas.milked.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.savvas.milked.MilkedApiApplication;
import org.savvas.milked.controller.error.ErrorResponse;
import org.savvas.milked.controller.request.GroupRequest;
import org.savvas.milked.controller.request.RegistrationRequest;
import org.savvas.milked.domain.MilkingGroup;
import org.savvas.milked.domain.MilkedUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.savvas.milked.controller.MilkedTestUtils.givenTheUserIsRegisteredAndActivated;
import static org.savvas.milked.controller.MilkedTestUtils.randomEmail;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MilkedApiApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port=0")
public class MilkingGroupControllerIntegrationTest {


    @Value("${local.server.port}")
    private int port;

    private final TestRestTemplate rest = new TestRestTemplate();
    private String baseUrl;
    private String createGroupUrl;

    @Before
    public void setUp() throws Exception {
        baseUrl = "http://localhost:" + port;
        createGroupUrl = baseUrl + "/group";
    }

    @Test
    public void creatingGroupReturnsCreatedResponseCode() {
        //given
        MilkedUser milkedUser = givenTheUserIsRegisteredAndActivated(rest, baseUrl, "savvas", "password");

        //when
        GroupRequest groupRequest = new GroupRequest(milkedUser.getId(), "SavvasGroup");
        ResponseEntity<String> createGroupResponse = rest.postForEntity(URI.create(createGroupUrl), groupRequest, String.class);
        String groupLocation = createGroupResponse.getHeaders().getFirst("Location");
        String groupUrl = baseUrl + groupLocation;
        ResponseEntity<MilkingGroup> groupResponse = rest.getForEntity((URI.create(groupUrl)), MilkingGroup.class);
        MilkingGroup milkingGroup = groupResponse.getBody();

        //then
        assertEquals(201, createGroupResponse.getStatusCode().value());
        assertThat(groupLocation).matches("^/group/\\d+$");
        assertEquals("SavvasGroup", milkingGroup.getName());
        assertEquals(milkedUser.getId(), milkingGroup.getOwner().getId());
        assertEquals("User that creates the group should automatically be added to it.", 1, milkingGroup.getMilkedUsers().size());
        assertEquals(milkedUser.getId(), milkingGroup.getMilkedUsers().get(0).getId());
    }

    @Test
    public void createGroupWithNullNameReturnsBadRequestResponseCode() {
        //given
        String baseUrl = "http://localhost:" + port;
        String createGroupUrl = baseUrl + "/group";
        String registrationUrl = baseUrl + "/registration";
        RegistrationRequest request = new RegistrationRequest(randomEmail(), "savvas", "password");
        //when
        ResponseEntity<String> registrationResponse = rest.postForEntity(URI.create(registrationUrl), request, String.class);
        String userPath = registrationResponse.getHeaders().getFirst("Location");
        ResponseEntity<MilkedUser> userResponse = rest.getForEntity(URI.create(baseUrl + userPath), MilkedUser.class);
        GroupRequest groupRequest = new GroupRequest(userResponse.getBody().getId(), null);
        ResponseEntity<ErrorResponse> createGroupResponse = rest.postForEntity(URI.create(createGroupUrl), groupRequest, ErrorResponse.class);
        //then
        assertEquals("Unexpected Error Message", 400, createGroupResponse.getStatusCode().value());
        assertEquals("Unexpected Error Message", "name", createGroupResponse.getBody().getErrors().get(0));
    }

    @Test
    public void createGroupWithMissingNameReturnsBadRequestResponseCode() {
        //given
        String baseUrl = "http://localhost:" + port;
        String createGroupUrl = baseUrl + "/group";
        String registrationUrl = baseUrl + "/registration";
        RegistrationRequest request = new RegistrationRequest(randomEmail(), "savvas", "password");
        //when
        ResponseEntity<String> registrationResponse = rest.postForEntity(URI.create(registrationUrl), request, String.class);
        String userPath = registrationResponse.getHeaders().getFirst("Location");
        ResponseEntity<MilkedUser> userResponse = rest.getForEntity(URI.create(baseUrl + userPath), MilkedUser.class);
        GroupRequest groupRequest = new GroupRequest(userResponse.getBody().getId(), "");
        ResponseEntity<ErrorResponse> createGroupResponse = rest.postForEntity(URI.create(createGroupUrl), groupRequest, ErrorResponse.class);
        //then
        assertEquals("Unexpected Error Message", 400, createGroupResponse.getStatusCode().value());
        assertEquals("Unexpected Error Message", "name", createGroupResponse.getBody().getErrors().get(0));

    }

    @Test
    public void createGroupWithNullIdReturnsBadRequestResponseCode() {
        //given
        String baseUrl = "http://localhost:" + port;
        String createGroupUrl = baseUrl + "/group";
        //when
        GroupRequest groupRequest = new GroupRequest(null, "savvasGroup");
        ResponseEntity<ErrorResponse> createGroupResponse = rest.postForEntity(URI.create(createGroupUrl), groupRequest, ErrorResponse.class);
        //then
        assertEquals("Unexpected Error Message", 400, createGroupResponse.getStatusCode().value());
        assertEquals("Unexpected Error Message", "userId", createGroupResponse.getBody().getErrors().get(0));
    }
}