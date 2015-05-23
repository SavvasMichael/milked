package org.savvas.milked.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.security.Principal;
import java.util.Map;

@Controller
public class FrontEndController {

    private static final Logger LOG = LoggerFactory.getLogger(FrontEndController.class);
    private static final String BASE_URL = "http://localhost:8080";
    private final RestTemplate restTemplate = new RestTemplate();


    @RequestMapping(value = "/activation/{uuid}", method = RequestMethod.GET)
    public String activateUser(@PathVariable("uuid") String uuid) {
        try {
            restTemplate.getForEntity(URI.create(BASE_URL + "/activation/" + uuid), String.class);
            return "successfulActivation";
        } catch (HttpClientErrorException e) {
            LOG.warn("Error when trying to activate user", e);
            return "errorLanding";
        }
    }

    @RequestMapping(value = "/anonymous-invite/{uuid}", method = RequestMethod.GET)
    public String activateAnonymousUser(@PathVariable("uuid") String uuid, Model model) {
        model.addAttribute(uuid);
        try {
            restTemplate.getForEntity(URI.create(BASE_URL + "/anonymous-invite/" + uuid), String.class);
            return "invitedUserAcceptLanding";
        } catch (HttpClientErrorException e) {
            LOG.warn("Error when trying to activate user", e);
            return "errorLanding";
        }
    }

    @RequestMapping(value = "/user/{uuid}/group/{groupId}/accept", method = RequestMethod.GET)
    public String acceptGroupInviteAndActivate(@PathVariable("uuid") String uuid, @PathVariable("groupId") Long groupId, Model model) {
        model.addAttribute(uuid);
        try {
            restTemplate.getForEntity(URI.create(BASE_URL + "/user/" + uuid + "/group/" + groupId + "/accept"), String.class);
            return "invitedUserAcceptLanding";
        } catch (HttpClientErrorException e) {
            LOG.warn("Error when trying to activate user", e);
            return "errorLanding";
        }
    }

    @RequestMapping(value = "/existing-user/{userId}/group/{groupId}/accept", method = RequestMethod.GET)
    public String acceptGroupInvite(@PathVariable("userId") String userId, @PathVariable("groupId") Long groupId) {
        try {
            restTemplate.getForEntity(URI.create(BASE_URL + "/existing-user/" + userId + "/group/" + groupId + "/accept"), String.class);
            return "landing";
        } catch (HttpClientErrorException e) {
            LOG.warn("Error when trying to activate user", e);
            return "errorLanding";
        }
    }

    @RequestMapping(value = "/user/{uuid}/update", method = RequestMethod.POST)
    public ResponseEntity updateUser(@PathVariable("uuid") String uuid, @RequestBody Map<String, String> invitedUserDetails) {
        try {
            return restTemplate.postForEntity(URI.create(BASE_URL + "/user/" + uuid + "/update"), invitedUserDetails, String.class);
        } catch (HttpClientErrorException e) {
            LOG.warn("Error when trying to activate user", e);
            return ResponseEntity.badRequest().body(e.getResponseBodyAsString());
        }
    }
}

