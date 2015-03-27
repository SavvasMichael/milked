package org.savvas.shafted.service;


import org.savvas.shafted.controller.request.GroupRequest;
import org.savvas.shafted.domain.Group;
import org.savvas.shafted.domain.GroupRepository;
import org.savvas.shafted.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupService {
    private GroupRepository groupRepository;

    public GroupService() {
    }
    @Autowired
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public Long createGroup(GroupRequest groupRequest) {
        Group group = new Group(groupRequest.getName(), groupRequest.getUserId());
        Group savedGroup = groupRepository.save(group);
        return savedGroup.getId();
    }

    public Group getGroup(Long id) {
        return groupRepository.findById(id);
    }
}
