package org.savvas.milked.controller;

import org.junit.runner.RunWith;
import org.junit.Test;
import org.savvas.milked.Application;
import org.savvas.milked.domain.MilkedUser;
import org.savvas.milked.domain.MilkingGroup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;

import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.savvas.milked.controller.MilkedTestUtils.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port=0")

public class UserControllerIntegrationTest {

    @Value("${local.server.port}")
    private int port;

    private final TestRestTemplate rest = new TestRestTemplate();

    @Test
    public void checkGetUserGroup(){
        //given
        String baseUrl = "http://localhost:" + port;
        MilkedUser joe = givenTheUserIsRegisteredAndActivated(rest, baseUrl, "savvas", "password");
        givenTheMilkingGroup(rest, baseUrl, joe.getId(), "joeGroup");
        givenTheMilkingGroup(rest, baseUrl, joe.getId(), "joeGroup2");

        MilkedUser savvas = givenTheUserIsRegisteredAndActivated(rest, baseUrl, "savvas", "password");
        givenTheMilkingGroup(rest, baseUrl, savvas.getId(), "savvasGroup");
        givenTheMilkingGroup(rest, baseUrl, savvas.getId(), "savvasGroup2");

        //when
        String savvasGroupsUrl = baseUrl + "/user/" + savvas.getId() + "/group";
        ResponseEntity<MilkingGroup[]> savvasGroupsResponse = rest.getForEntity(URI.create(savvasGroupsUrl), MilkingGroup[].class);
        MilkingGroup[] savvasGroups = savvasGroupsResponse.getBody();
        //then
        assertThat(savvasGroups).hasSize(2);

        String joeGroupsUrl = baseUrl + "/user/" + joe.getId() + "/group";
        ResponseEntity<MilkingGroup[]> joeGroupsResponse = rest.getForEntity(URI.create(savvasGroupsUrl), MilkingGroup[].class);
        MilkingGroup[] joeGroups = joeGroupsResponse.getBody();
        //then
        assertThat(joeGroups).hasSize(2);


    }
}