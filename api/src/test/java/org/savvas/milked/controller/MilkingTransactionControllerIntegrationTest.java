package org.savvas.milked.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.savvas.milked.controller.MilkedTestUtils.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.savvas.milked.Application;
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
@SpringApplicationConfiguration(classes = Application.class)
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
        MilkingGroup group = givenTheMilkingGroup(rest, baseUrl, savvas.getId(), "savvasgroup");
        givenTheUserHasJoinedTheGroup(rest, baseUrl, savvasFriend.getId(), group.getId());
        String milkUrl = baseUrl + "/group/" + group.getId() + "/milk";
        MilkingTransactionRequest milkSavvasFriend = new MilkingTransactionRequest(group.getId(), savvas.getId(), savvasFriend.getId(), 5, "Beer");
        MilkingTransactionRequest milkSavvas = new MilkingTransactionRequest(group.getId(), savvasFriend.getId(), savvas.getId(), 1, "Water");
        //when
        ResponseEntity<MilkingTransaction> milkSavvasFriendResponse = rest.postForEntity(URI.create(milkUrl), milkSavvasFriend, MilkingTransaction.class);
        ResponseEntity<String> milkSavvasResponse = rest.postForEntity(URI.create(milkUrl), milkSavvas, String.class);
        MilkingTransaction[] fetchedMilkedTransactions = rest.getForEntity((URI.create(milkUrl)), MilkingTransaction[].class).getBody();
        //then
        assertEquals(201, milkSavvasFriendResponse.getStatusCode().value());
        assertEquals(201, milkSavvasResponse.getStatusCode().value());
        assertThat(fetchedMilkedTransactions).hasSize(2);
        assertEquals("Unexpected Description", milkSavvas.getDescription(), fetchedMilkedTransactions[1].getDescription());
        assertEquals("Unexpected Description", milkSavvasFriend.getDescription(), fetchedMilkedTransactions[0].getDescription());
        assertEquals("Unexpected Milker id", milkSavvas.getMilkerId(), fetchedMilkedTransactions[1].getMilker().getId());
        assertEquals("Unexpected Milker id", milkSavvasFriend.getMilkerId(), fetchedMilkedTransactions[0].getMilker().getId());
        assertEquals("Unexpected Milkee id", milkSavvas.getMilkeeId(), fetchedMilkedTransactions[1].getMilkee().getId());
        assertEquals("Unexpected Milkee id", milkSavvasFriend.getMilkeeId(), fetchedMilkedTransactions[0].getMilkee().getId());
        assertEquals("Unexpected Amount", milkSavvas.getAmount(), fetchedMilkedTransactions[1].getAmount());
        assertEquals("Unexpected Amount", milkSavvasFriend.getAmount(), fetchedMilkedTransactions[0].getAmount());
        assertEquals("Unexpected Group id", milkSavvas.getGroupId(), fetchedMilkedTransactions[1].getMilkingGroupId());
        assertEquals("Unexpected Group id", milkSavvasFriend.getGroupId(), fetchedMilkedTransactions[0].getMilkingGroupId());
    }
}