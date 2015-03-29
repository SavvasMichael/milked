package org.savvas.shafted.service;

import org.savvas.shafted.controller.error.NotFoundException;
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

    public GroupUser getGroup(Long id) {
        GroupUser groupUser = groupUserRepository.findOne(id);
        if (groupUser == null) {
            throw new NotFoundException("User is not a member of this group");
        }
        return groupUser;
    }

    public Long createGroupUser(GroupUserRequest groupUserRequest) {
        GroupUser groupUser = new GroupUser(groupUserRequest.getUserId(), groupUserRequest.getGroupId());
        groupUser.setState(GroupUserState.INVITED);
        GroupUser savedGroupUser = groupUserRepository.save(groupUser);
        return savedGroupUser.getGroupId();
    }

    public Long activateGroupUser(Long id) {
        GroupUser activatedUser = groupUserRepository.findOne(id);
        activatedUser.setState(GroupUserState.MEMBER);
        GroupUser activatedSavedUser = groupUserRepository.save(activatedUser);
        return activatedSavedUser.getId();
    }

    public void deleteGroupUser(Long id) {
        groupUserRepository.delete(id);
    }
}
