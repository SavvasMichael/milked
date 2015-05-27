package org.savvas.milked.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
public class FrontEndRestController {

    private static final Logger LOG = LoggerFactory.getLogger(FrontEndRestController.class);
    private static final String BASE_URL = "http://localhost:8080";
    private final RestTemplate restTemplate = new RestTemplate();
    private final BalanceCalculator balanceCalculator;
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Autowired
    public FrontEndRestController(BalanceCalculator balanceCalculator) {
        this.balanceCalculator = balanceCalculator;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ResponseEntity registerUser(@RequestBody Map body) {
        try {
            return restTemplate.postForEntity(URI.create(BASE_URL + "/registration"), body, Map.class);
        } catch (HttpClientErrorException e) {
            LOG.warn("Error when trying to register user", e);
            return ResponseEntity.badRequest().body(e.getResponseBodyAsString());
        }
    }

    @RequestMapping(value = "/group", method = RequestMethod.GET)
    public ResponseEntity getGroups(Principal principal) {
        Authentication authentication = (Authentication) principal;
        GrantedAuthority authority = authentication.getAuthorities().iterator().next();
        String userId = authority.getAuthority();
        try {
            return restTemplate.getForEntity(URI.create(BASE_URL + "/user/" + userId + "/group"), List.class);
        } catch (HttpClientErrorException e) {
            LOG.warn("Error when trying to fetch groups", e);
            return ResponseEntity.badRequest().body(e.getResponseBodyAsString());
        }
    }

    @RequestMapping(value = "/group", method = RequestMethod.POST)
    public ResponseEntity createGroup(@RequestBody Map<String, String> body, Principal principal) {
        Authentication authentication = (Authentication) principal;
        GrantedAuthority authority = authentication.getAuthorities().iterator().next();
        String userId = authority.getAuthority();
        body.put("userId", userId);
        try {
            return restTemplate.postForEntity(URI.create(BASE_URL + "/group"), body, Map.class);
        } catch (HttpClientErrorException e) {
            LOG.warn("Error when trying to fetch groups", e);
            return ResponseEntity.badRequest().body(e.getResponseBodyAsString());
        }
    }

    @RequestMapping(value = "/group/{groupId}/users", method = RequestMethod.GET)
    public ResponseEntity getGroupMembers(@PathVariable("groupId") Long groupId, Principal principal) {
        Authentication authentication = (Authentication) principal;
        GrantedAuthority authority = authentication.getAuthorities().iterator().next();
        String userId = authority.getAuthority();
        Long loggedInUserId = Long.valueOf(userId);
        try {
            MilkedUser[] milkedUsers = restTemplate.getForEntity(URI.create(BASE_URL + "/group/" + groupId + "/users"), MilkedUser[].class).getBody();
            MilkedUser loggedInUser = null;
            for (MilkedUser user : milkedUsers) {
                if (loggedInUserId.equals(user.getId())) {
                    loggedInUser = user;
                }
            }
            MilkingTransaction[] milkingTransactions = restTemplate.getForEntity(URI.create(BASE_URL + "/group/" + groupId + "/milk"), MilkingTransaction[].class).getBody();
            balanceCalculator.calculateBalances(milkedUsers, milkingTransactions);
            return ResponseEntity.ok().body(new GroupDetails(milkedUsers, milkingTransactions, loggedInUser));
        } catch (HttpClientErrorException e) {
            LOG.warn("Error when trying to fetch group members", e);
            return ResponseEntity.badRequest().body(e.getResponseBodyAsString());
        }
    }

    @RequestMapping(value = "/group/{groupId}/leave", method = RequestMethod.POST)
    public ResponseEntity leaveGroup(@PathVariable("groupId") Long groupId, Principal principal) {
        Authentication authentication = (Authentication) principal;
        GrantedAuthority authority = authentication.getAuthorities().iterator().next();
        String userId = authority.getAuthority();
        try {
            return restTemplate.postForEntity(URI.create(BASE_URL + "/user/" + userId + "/group/" + groupId + "/leave"), null, Map.class);
        } catch (HttpClientErrorException e) {
            LOG.warn("Error when deleting group", e);
            return ResponseEntity.badRequest().body(e.getResponseBodyAsString());
        }
    }

    @RequestMapping(value = "/group/{groupId}/invite", method = RequestMethod.POST)
    public ResponseEntity inviteUser(@RequestBody Map<String, String> emailBody, @PathVariable("groupId") Long groupId) {
        if (!emailBody.get("email").matches(EMAIL_PATTERN)) {
            return ResponseEntity.badRequest().build();
        }
        try {
            return restTemplate.postForEntity(URI.create(BASE_URL + "/group/" + groupId + "/invite/"), emailBody, Map.class);
        } catch (HttpClientErrorException e) {
            LOG.warn("Error when inviting user", e);
            return ResponseEntity.badRequest().body(e.getResponseBodyAsString());
        }
    }

    @RequestMapping(value = "/group/{groupId}/milk", method = RequestMethod.POST)
    public ResponseEntity milk(@RequestBody Map milkRequest, @PathVariable("groupId") Long groupId, Principal principal) {
        Authentication authentication = (Authentication) principal;
        GrantedAuthority authority = authentication.getAuthorities().iterator().next();
        String milkerId = authority.getAuthority();
        milkRequest.put("milkerId", milkerId);
        try {
            return restTemplate.postForEntity(URI.create(BASE_URL + "/group/" + groupId + "/milk"), milkRequest, Map.class);
        } catch (HttpClientErrorException e) {
            LOG.warn("Error when processing milking transaction", e);
            return ResponseEntity.badRequest().body(e.getResponseBodyAsString());
        }
    }
    @RequestMapping(value = "/group/{groupId}/milked", method = RequestMethod.POST)
    public ResponseEntity milked(@RequestBody Map<String, String> milkRequest, @PathVariable("groupId") Long groupId, Principal principal) {
        Authentication authentication = (Authentication) principal;
        GrantedAuthority authority = authentication.getAuthorities().iterator().next();
        String milkeeId = authority.getAuthority();
        milkRequest.put("milkeeId", milkeeId);
        String amount = milkRequest.get("amount");
        for (int i = 0; i < amount.length(); i++) {
            if (amount.charAt(i) == '.') {
                float floatAmount = Float.valueOf(amount);
                floatAmount = floatAmount * 100;
                int intAmount = (int) floatAmount;
                Math.round(intAmount);
                String finalAmount = String.valueOf(intAmount);
                milkRequest.put("amount", finalAmount);
            }
        }
        try {
            return restTemplate.postForEntity(URI.create(BASE_URL + "/group/" + groupId + "/milk"), milkRequest, String.class);
        } catch (HttpClientErrorException e) {
            LOG.warn("Error when processing milking transaction", e);
            return ResponseEntity.badRequest().body(e.getResponseBodyAsString());
        }
    }

    @RequestMapping(value = "/group/{groupId}/milk", method = RequestMethod.GET)
    public ResponseEntity getMilkingTransactions(@PathVariable("groupId") Long groupId) {
        try {
            return restTemplate.getForEntity(URI.create(BASE_URL + "/group/" + groupId + "/milk"), MilkingTransaction[].class);
        } catch (HttpClientErrorException e) {
            LOG.warn("Error when getting milking transaction", e);
            return ResponseEntity.badRequest().body(e.getResponseBodyAsString());
        }
    }

    @RequestMapping(value = "/user/forgot-password", method = RequestMethod.POST)
    public ResponseEntity recoverPassword(@RequestBody Map<String, String> emailBody) {
        try {
            return restTemplate.postForEntity(URI.create(BASE_URL + "/user/forgot-password"), emailBody, String.class);
        } catch (HttpClientErrorException e) {
            LOG.warn("Error when recovering password", e);
            return ResponseEntity.badRequest().body(e.getResponseBodyAsString());
        }
    }
}