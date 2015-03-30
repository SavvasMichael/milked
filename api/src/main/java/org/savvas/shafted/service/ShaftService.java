package org.savvas.shafted.service;

import org.savvas.shafted.controller.request.ShaftRequest;
import org.savvas.shafted.domain.GroupUser;
import org.savvas.shafted.domain.GroupUserRepository;
import org.savvas.shafted.domain.Shaft;
import org.savvas.shafted.domain.ShaftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShaftService {

    private ShaftRepository shaftRepository;

    ShaftService() {
    }
    @Autowired
    public ShaftService(ShaftRepository shaftRepository) {
        this.shaftRepository = shaftRepository;
    }

    public Long createShaft(ShaftRequest shaftRequest) {
        Shaft shaft = new Shaft(shaftRequest.getShafterId(), shaftRequest.getShafteeId(),
                shaftRequest.getGroupId(), shaftRequest.getAmount());
        Shaft savedShaft = shaftRepository.save(shaft);
        return savedShaft.getId();
    }
    public Shaft getShaft(Long id) {
        return shaftRepository.findOne(id);
    }
}
