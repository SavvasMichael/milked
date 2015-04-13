package org.savvas.milked.service;

import org.savvas.milked.controller.error.NotFoundException;
import org.savvas.milked.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<GroupInvite> getGroupInvites(Long userId) {
        List<GroupInvite> groupInvites = groupInviteRepository.findByUserId(userId);
        if (groupInvites == null) {
            throw new NotFoundException("User is not a member of this group");
        }
        return groupInvites;
    }

    public Long inviteGroupUser(Long groupId, Long userId) {
        MilkingGroup fetchedGroup = milkingGroupRepository.findById(groupId);
        MilkedUser fetchedUser = milkedUserRepository.findOne(userId);
        GroupInvite groupInvite = new GroupInvite(fetchedGroup.getId(), fetchedUser.getId());
        GroupInvite savedGroupInvite = groupInviteRepository.save(groupInvite);
        //TODO: Send Email
        return savedGroupInvite.getGroupId();
    }

    public MilkingGroup acceptGroupInvite(Long userId, Long groupId) {
        MilkingGroup group = milkingGroupRepository.findOne(groupId);
        MilkedUser user = milkedUserRepository.findOne(userId);
        group.addUserToGroup(user);
        milkingGroupRepository.save(group);
        GroupInvite invite = groupInviteRepository.findOneByGroupIdAndUserId(groupId, userId);
        groupInviteRepository.delete(invite);
        return group;
    }

    public void declineInvitation(Long userId, Long groupId) {
        GroupInvite invite = groupInviteRepository.findOneByGroupIdAndUserId(groupId, userId);
        groupInviteRepository.delete(invite);
    }
}

