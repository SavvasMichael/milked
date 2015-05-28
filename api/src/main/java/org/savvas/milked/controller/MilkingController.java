package org.savvas.milked.controller;

import com.sun.xml.internal.ws.developer.MemberSubmissionAddressing;
import org.savvas.milked.controller.error.ValidationException;
import org.savvas.milked.controller.request.MilkingTransactionRequest;
import org.savvas.milked.domain.MilkingTransaction;
import org.savvas.milked.service.MilkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@RestController
public class MilkingController {

    private final MilkingService milkingService;

    @Autowired
    public MilkingController(MilkingService milkingService) {
        this.milkingService = milkingService;
    }

    @RequestMapping(value = "/group/{groupId}/milk", method = RequestMethod.POST)
    public ResponseEntity milkTheUser(@PathVariable("groupId") @RequestBody Long groupId, @RequestBody MilkingTransactionRequest milkingTransactionRequest) {
        if (milkingTransactionRequest.getMilkeeId() == milkingTransactionRequest.getMilkerId()) {
            throw new ValidationException("Same milker and milkee ids");
        }
        Integer amount = milkingTransactionRequest.getAmount();
        milkingTransactionRequest.setAmount(amount);
        Long transactionId = milkingService.createMilkingTransaction(milkingTransactionRequest, groupId);
        URI transactionLocationURI = URI.create("/milk/" + groupId + "/" + transactionId);
        return ResponseEntity.created(transactionLocationURI).build();
    }
    @RequestMapping(value = "/group/{groupId}/milk", method = RequestMethod.GET)
    public List<MilkingTransaction> getMilkingTransaction(@PathVariable("groupId") Long groupId) {
        return milkingService.getMilkingTransactions(groupId);
    }
}
