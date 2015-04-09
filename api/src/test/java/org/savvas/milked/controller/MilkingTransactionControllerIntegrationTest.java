package org.savvas.milked.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.savvas.milked.controller.MilkedTestUtils.givenTheMilkingGroup;
import static org.savvas.milked.controller.MilkedTestUtils.givenTheUserHasJoinedTheGroup;
import static org.savvas.milked.controller.MilkedTestUtils.givenTheUserIsRegisteredAndActivated;

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
    public void createShaftReturns201ResponseCode() {
        //given
        String baseUrl = "http://localhost:" + port;
        String shaftUrl = baseUrl + "/shaft";
        //when
        MilkingTransactionRequest milkingTransactionRequest = new MilkingTransactionRequest(1L, 2L, 5L, 1);
        ResponseEntity<String> shaftResponse = rest.postForEntity(URI.create(shaftUrl), milkingTransactionRequest, String.class);
        String shaftLocation = shaftResponse.getHeaders().getFirst("Location");
        //then
        assertEquals(201, shaftResponse.getStatusCode().value());
        assertThat(shaftLocation).matches("^/shaft/\\d+$");
    }

    @Test
    public void createMilkingTransactionReturnsCorrectBodyFieldElements() {
        //given
        String baseUrl = "http://localhost:" + port;
        String shaftUrl = baseUrl + "/shaft";

        MilkedUser milker = givenTheUserIsRegisteredAndActivated(rest, baseUrl, "savvas", "password");
        MilkedUser milkee = givenTheUserIsRegisteredAndActivated(rest, baseUrl, "michael", "password");
        MilkingGroup milkerGroup = givenTheMilkingGroup(rest, baseUrl, milker.getId(), "SavvasGroup");
        givenTheUserHasJoinedTheGroup(rest, baseUrl, milkee.getId(), milkerGroup.getId());

        //when
        MilkingTransactionRequest milkingTransactionRequest = new MilkingTransactionRequest(milker.getId(), milkee.getId(), milkerGroup.getId(), 100);
        ResponseEntity<String> milkingTransactionResponse = rest.postForEntity(URI.create(shaftUrl), milkingTransactionRequest, String.class);
        String milkedTransactionLocation = milkingTransactionResponse.getHeaders().getFirst("Location");
        ResponseEntity<MilkingTransaction> milkedTransactionResponse = rest.getForEntity((URI.create(baseUrl + milkedTransactionLocation)), MilkingTransaction.class);

        //then
        assertThat(milkedTransactionLocation).matches("^/shaft/\\d+$");
        MilkingTransaction milkingTransaction = milkedTransactionResponse.getBody();
        assertEquals("Unexpected Group Id", milkerGroup.getId(), milkingTransaction.getGroup().getId());
        assertEquals("Unexpected Milker Id", milker.getId(), milkingTransaction.getMilker().getId());
        assertEquals("Unexpected Milkee Id", milkee.getId(), milkingTransaction.getMilkee().getId());
        assertEquals("Unexpected Transaction Amount", 100, milkingTransaction.getAmount().intValue());
    }
}