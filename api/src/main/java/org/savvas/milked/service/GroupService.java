package org.savvas.milked.service;


import org.savvas.milked.controller.request.GroupRequest;
import org.savvas.milked.domain.MilkedUser;
import org.savvas.milked.domain.MilkedUserRepository;
import org.savvas.milked.domain.MilkingGroup;
import org.savvas.milked.domain.MilkingGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupService {

    private final MilkingGroupRepository milkingGroupRepository;
    private final MilkedUserRepository milkedUserRepository;

    @Autowired
    public GroupService(MilkingGroupRepository milkingGroupRepository, MilkedUserRepository milkedUserRepository) {
        this.milkingGroupRepository = milkingGroupRepository;
        this.milkedUserRepository = milkedUserRepository;
    }

    public Long createGroup(GroupRequest groupRequest) {
        MilkedUser fetchedUser = milkedUserRepository.findOne(groupRequest.getUserId());
        MilkingGroup milkingGroup = new MilkingGroup(groupRequest.getName(), fetchedUser);
        milkingGroup.addUserToGroup(fetchedUser);
        MilkingGroup savedMilkingGroup = milkingGroupRepository.save(milkingGroup);
        return savedMilkingGroup.getId();
    }

    public MilkingGroup getGroup(Long id) {
        return milkingGroupRepository.findById(id);
    }
}
