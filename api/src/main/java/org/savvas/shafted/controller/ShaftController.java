package org.savvas.shafted.controller;

import org.savvas.shafted.controller.request.ShaftRequest;
import org.savvas.shafted.domain.Shaft;
import org.savvas.shafted.domain.ShaftRepository;
import org.savvas.shafted.service.ShaftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ShaftController {

    private final ShaftService shaftService;

    @Autowired
    public ShaftController(ShaftService shaftService) {
        this.shaftService = shaftService;
    }

    @RequestMapping(value = "/shaft", method = RequestMethod.POST)
    public ResponseEntity shaftUser(@RequestBody ShaftRequest shaftRequest) {
        Long shaftId = shaftService.createShaft(shaftRequest);
        URI shaftLocationURI = URI.create("/shaft/" + shaftId);
        return ResponseEntity.created(shaftLocationURI).build();
    }
    @RequestMapping(value = "/shaft/{id}", method = RequestMethod.GET)
    public Shaft getShaft(@PathVariable("id") Long id) {
        return shaftService.getShaft(id);
    }
}
