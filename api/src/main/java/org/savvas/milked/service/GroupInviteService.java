package org.savvas.milked.service;

import org.savvas.milked.controller.error.NotFoundException;
import org.savvas.milked.controller.request.GroupInviteRequest;
import org.savvas.milked.controller.request.GroupUserState;
import org.savvas.milked.domain.GroupInvite;
import org.savvas.milked.domain.GroupInviteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupInviteService {
    private GroupInviteRepository groupInviteRepository;

    @Autowired
    public GroupInviteService(GroupInviteRepository groupInviteRepository) {
        this.groupInviteRepository = groupInviteRepository;
    }

    public GroupInvite getGroupInvite(Long id) {
        GroupInvite groupInvite = groupInviteRepository.findOne(id);
        if (groupInvite == null) {
            throw new NotFoundException("User is not a member of this group");
        }
        return groupInvite;
    }

    public Long createGroupUser(GroupInviteRequest groupInviteRequest) {
        GroupInvite groupInvite = new GroupInvite(groupInviteRequest.getUserId(), groupInviteRequest.getGroupId());
        groupInvite.setState(GroupUserState.INVITED);
        GroupInvite savedGroupInvite = groupInviteRepository.save(groupInvite);
        return savedGroupInvite.getGroupId();
    }

    public Long activateGroupUser(Long id) {
        GroupInvite activatedUser = groupInviteRepository.findOne(id);
        activatedUser.setState(GroupUserState.MEMBER);
        GroupInvite activatedSavedUser = groupInviteRepository.save(activatedUser);
        return activatedSavedUser.getId();
    }

    public void deleteGroupUser(Long id) {
        groupInviteRepository.delete(id);
    }
}
