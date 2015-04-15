package org.savvas.milked.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.savvas.milked.controller.MilkedTestUtils.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.savvas.milked.MilkedApiApplication;
import org.savvas.milked.controller.request.MilkingTransactionRequest;
import org.savvas.milked.domain.MilkingTransaction;
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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MilkedApiApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port=0")

public class MilkingTransactionControllerIntegrationTest {

    @Value("${local.server.port}")
    private int port;

    private final TestRestTemplate rest = new TestRestTemplate();

    @Test
    public void milkingTransactionSavesCorrectFieldsAndReturs201() {
        //given
        String baseUrl = "http://localhost:" + port;
        MilkedUser savvas = givenTheUserIsRegisteredAndActivated(rest, baseUrl, "savvas", "pass");
        MilkedUser savvasFriend = givenTheUserIsRegisteredAndActivated(rest, baseUrl, "savvasfriend", "pass");
        MilkedUser joe = givenTheUserIsRegisteredAndActivated(rest, baseUrl, "joe", "pass");

        MilkingGroup savvasGroup = givenTheMilkingGroup(rest, baseUrl, savvas.getId(), "savvasgroup");
        MilkingGroup joeGroup = givenTheMilkingGroup(rest, baseUrl, savvasFriend.getId(), "savvasfriendgroup");

        givenTheUserHasJoinedTheGroup(rest, baseUrl, savvasFriend.getId(), savvasGroup.getId());
        givenTheUserHasJoinedTheGroup(rest, baseUrl, savvas.getId(), joeGroup.getId());

        String savvasGroupMilkUrl = baseUrl + "/group/" + savvasGroup.getId() + "/milk";
        String joeGroupMilkUrl = baseUrl + "/group/" + joeGroup.getId() + "/milk";

        MilkingTransactionRequest milkSavvasFriend = new MilkingTransactionRequest(savvasGroup.getId(), savvas.getId(), savvasFriend.getId(), 5, "Beer");
        MilkingTransactionRequest milkSavvas = new MilkingTransactionRequest(savvasGroup.getId(), savvasFriend.getId(), savvas.getId(), 1, "Water");
        MilkingTransactionRequest milkJoe = new MilkingTransactionRequest(joeGroup.getId(), savvas.getId(), joe.getId(), 2, "Pie");
        //when

        ResponseEntity<MilkingTransaction> milkSavvasFriendResponse = rest.postForEntity(URI.create(savvasGroupMilkUrl), milkSavvasFriend, MilkingTransaction.class);
        ResponseEntity<String> milkSavvasResponse = rest.postForEntity(URI.create(savvasGroupMilkUrl), milkSavvas, String.class);
        ResponseEntity<String> milkJoeResponse = rest.postForEntity(URI.create(joeGroupMilkUrl), milkJoe, String.class);
        MilkingTransaction[] fetchedSavvasGroupMilkedTransactions = rest.getForEntity((URI.create(savvasGroupMilkUrl)), MilkingTransaction[].class).getBody();
        MilkingTransaction[] fetchedJoeGroupMilkedTransactions = rest.getForEntity((URI.create(joeGroupMilkUrl)), MilkingTransaction[].class).getBody();

        //then
        assertEquals(201, milkSavvasFriendResponse.getStatusCode().value());
        assertEquals(201, milkSavvasResponse.getStatusCode().value());
        assertEquals(201, milkJoeResponse.getStatusCode().value());
        assertEquals("Unexpected Description", milkSavvas.getDescription(), fetchedSavvasGroupMilkedTransactions[1].getDescription());
        assertEquals("Unexpected Description", milkSavvasFriend.getDescription(), fetchedSavvasGroupMilkedTransactions[0].getDescription());

        assertEquals("Unexpected Milker id", milkSavvas.getMilkerId(), fetchedSavvasGroupMilkedTransactions[1].getMilker().getId());
        assertEquals("Unexpected Milker id", milkSavvasFriend.getMilkerId(), fetchedSavvasGroupMilkedTransactions[0].getMilker().getId());

        assertEquals("Unexpected Milkee id", milkSavvas.getMilkeeId(), fetchedSavvasGroupMilkedTransactions[1].getMilkee().getId());
        assertEquals("Unexpected Milkee id", milkSavvasFriend.getMilkeeId(), fetchedSavvasGroupMilkedTransactions[0].getMilkee().getId());

        assertEquals("Unexpected Amount", milkSavvas.getAmount(), fetchedSavvasGroupMilkedTransactions[1].getAmount());
        assertEquals("Unexpected Amount", milkSavvasFriend.getAmount(), fetchedSavvasGroupMilkedTransactions[0].getAmount());

        assertEquals("Unexpected Group id", milkSavvas.getGroupId(), fetchedSavvasGroupMilkedTransactions[1].getMilkingGroupId());
        assertEquals("Unexpected Group id", milkSavvasFriend.getGroupId(), fetchedSavvasGroupMilkedTransactions[0].getMilkingGroupId());

        assertThat(fetchedSavvasGroupMilkedTransactions).hasSize(2);

        assertEquals("Unexpected Description", milkJoe.getDescription(), fetchedJoeGroupMilkedTransactions[0].getDescription());
        assertEquals("Unexpected Milker Id", milkJoe.getMilkerId(), fetchedJoeGroupMilkedTransactions[0].getMilker().getId());
        assertEquals("Unexpected Milkee Id", milkJoe.getMilkeeId(), fetchedJoeGroupMilkedTransactions[0].getMilkee().getId());
        assertEquals("Unexpected Amount", milkJoe.getAmount(), fetchedJoeGroupMilkedTransactions[0].getAmount());
        assertEquals("Unexpected Group Id", milkJoe.getGroupId(), fetchedJoeGroupMilkedTransactions[0].getMilkingGroupId());

        assertThat(fetchedJoeGroupMilkedTransactions).hasSize(1);
    }
}