package org.savvas.shafted.service;


import org.savvas.shafted.controller.request.GroupRequest;
import org.savvas.shafted.domain.ShaftGroup;
import org.savvas.shafted.domain.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupService {
    private GroupRepository groupRepository;

    GroupService() {
    }

    @Autowired
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public Long createGroup(GroupRequest groupRequest) {
        ShaftGroup shaftGroup = new ShaftGroup(groupRequest.getName(), groupRequest.getUserId());
        ShaftGroup savedShaftGroup = groupRepository.save(shaftGroup);
        return savedShaftGroup.getId();
    }

    public ShaftGroup getGroup(Long id) {
        return groupRepository.findById(id);
    }
}
