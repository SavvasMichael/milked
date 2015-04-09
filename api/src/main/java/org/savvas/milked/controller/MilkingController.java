package org.savvas.milked.controller;

import org.savvas.milked.controller.request.MilkingTransactionRequest;
import org.savvas.milked.domain.MilkingTransaction;
import org.savvas.milked.service.MilkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class MilkingController {

    private final MilkingService milkingService;

    @Autowired
    public MilkingController(MilkingService milkingService) {
        this.milkingService = milkingService;
    }

    @RequestMapping(value = "/shaft", method = RequestMethod.POST)
    public ResponseEntity milkUser(@RequestBody MilkingTransactionRequest milkingTransactionRequest) {
        Long transactionId = milkingService.createMilkingTransaction(milkingTransactionRequest);
        URI transactionLocationURI = URI.create("/shaft/" + transactionId);
        return ResponseEntity.created(transactionLocationURI).build();
    }

    @RequestMapping(value = "/shaft/{id}", method = RequestMethod.GET)
    public MilkingTransaction getMilkingTransaction(@PathVariable("id") Long id) {
        return milkingService.getMilkingTransaction(id);
    }
}
