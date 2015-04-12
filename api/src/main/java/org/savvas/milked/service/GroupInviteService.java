package org.savvas.milked.service;

import org.savvas.milked.controller.error.NotFoundException;
import org.savvas.milked.controller.request.GroupInviteRequest;
import org.savvas.milked.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupInviteService {
    private GroupInviteRepository groupInviteRepository;
    private MilkedUserRepository milkedUserRepository;
    private MilkingGroupRepository milkingGroupRepository;

    @Autowired
    public GroupInviteService(GroupInviteRepository groupInviteRepository, MilkedUserRepository milkedUserRepository, MilkingGroupRepository milkingGroupRepository) {
        this.groupInviteRepository = groupInviteRepository;
        this.milkedUserRepository = milkedUserRepository;
        this.milkingGroupRepository = milkingGroupRepository;
    }
    public GroupInvite getGroupInvite(Long id) {
        GroupInvite groupInvite = groupInviteRepository.findOne(id);
        if (groupInvite == null) {
            throw new NotFoundException("User is not a member of this group");
        }
        return groupInvite;
    }

    public Long inviteGroupUser(GroupInviteRequest groupInviteRequest) {
        GroupInvite groupInvite = new GroupInvite(groupInviteRequest.getUserId(), groupInviteRequest.getGroupId());
        GroupInvite savedGroupInvite = groupInviteRepository.save(groupInvite);
        return savedGroupInvite.getGroupId();
    }

    public Long activateGroupUser(String uuid) {
//        MilkedUser milkedUser = milkedUserRepository.findByUuid(uuid);
//        GroupInvite activatedUser = groupInviteRepository.findOne(milkedUser.getId());
//        activatedUser.setState(GroupUserState.MEMBER);
//        MilkingGroup milkingGroup = milkingGroupRepository.findById(activatedUser.getGroupId());
//        milkingGroup.addUserToGroup(milkedUser);
//        GroupInvite activatedSavedUser = groupInviteRepository.save(activatedUser);
//        return activatedSavedUser.getId();
        return 1l;
    }
    public void deleteGroupUser(Long id) {
        groupInviteRepository.delete(id);
    }
}

