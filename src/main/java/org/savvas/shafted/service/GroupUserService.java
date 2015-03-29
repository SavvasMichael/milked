package org.savvas.shafted.service;

import org.savvas.shafted.controller.request.GroupUserRequest;
import org.savvas.shafted.controller.request.GroupUserState;
import org.savvas.shafted.domain.GroupRepository;
import org.savvas.shafted.domain.GroupUser;
import org.savvas.shafted.domain.GroupUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupUserService {
    private GroupUserRepository groupUserRepository;

    GroupUserService() {
    }

    @Autowired
    public GroupUserService(GroupUserRepository groupUserRepository) {
        this.groupUserRepository = groupUserRepository;
    }

    public Long createGroupUser(GroupUserRequest groupUserRequest) {
        GroupUser groupUser = new GroupUser(groupUserRequest.getUserId(), groupUserRequest.getGroupId());
        groupUser.setState(GroupUserState.INVITED);
        GroupUser savedGroupUser = groupUserRepository.save(groupUser);
        return savedGroupUser.getId();
    }

    public GroupUser getGroup(Long id) {
        return groupUserRepository.findOne(id);
    }
}
