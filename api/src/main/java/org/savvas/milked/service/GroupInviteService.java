package org.savvas.milked.service;

import org.savvas.milked.controller.error.NotFoundException;
import org.savvas.milked.controller.error.ValidationException;
import org.savvas.milked.controller.request.RegistrationRequest;
import org.savvas.milked.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupInviteService {
    private GroupInviteRepository groupInviteRepository;
    private MilkedUserRepository milkedUserRepository;
    private MilkingGroupRepository milkingGroupRepository;
    private UserService userService;
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Autowired
    public GroupInviteService(GroupInviteRepository groupInviteRepository, MilkedUserRepository milkedUserRepository, MilkingGroupRepository milkingGroupRepository, UserService userService) {
        this.groupInviteRepository = groupInviteRepository;
        this.milkedUserRepository = milkedUserRepository;
        this.milkingGroupRepository = milkingGroupRepository;
        this.userService = userService;
    }

    public List<GroupInvite> getGroupInvites(Long userId) {
        List<GroupInvite> groupInvites = groupInviteRepository.findByUserId(userId);
        if (groupInvites == null) {
            throw new NotFoundException("User is not a member of this group");
        }
        return groupInvites;
    }

    public MilkedUser inviteGroupUser(Long groupId, String email) {
        String randomPass = "pass" + Math.random();
        if (email.isEmpty() || email == null) {
            throw new ValidationException("Empty or null email");
        }
        MilkingGroup fetchedGroup = milkingGroupRepository.findById(groupId);
        MilkedUser fetchedUser = milkedUserRepository.findByEmail(email);
        if (fetchedUser != null) {
            if (fetchedGroup.getMilkedUsers().contains(fetchedUser)) {
                throw new ValidationException("User is already a member");
            }
            userService.sendInvitationEmail(fetchedUser, groupId);
        }
        if (fetchedUser == null) {
            RegistrationRequest registrationRequest = new RegistrationRequest(email, "", randomPass);
            fetchedUser = userService.createInvitedUser(registrationRequest, groupId);
        }
        GroupInvite groupInvite = new GroupInvite(fetchedGroup.getId(), fetchedUser.getId(), fetchedUser.getUuid());
        groupInviteRepository.save(groupInvite);
        return fetchedUser;
    }

    public MilkingGroup acceptGroupInvite(String uuid, Long groupId) {
        MilkingGroup group = milkingGroupRepository.findOne(groupId);
        MilkedUser user = milkedUserRepository.findByUuid(uuid);
        group.addUserToGroup(user);
        milkingGroupRepository.save(group);
        GroupInvite invite = groupInviteRepository.findOneByGroupIdAndUserId(groupId, user.getId());
        groupInviteRepository.delete(invite);
        return group;
    }

    public MilkingGroup acceptGroupInviteForAnonymous(String uuid, Long groupId) {
        MilkingGroup group = milkingGroupRepository.findOne(groupId);
        MilkedUser user = milkedUserRepository.findByUuid(uuid);
        group.addUserToGroup(user);
        milkingGroupRepository.save(group);
        GroupInvite invite = groupInviteRepository.findOneByGroupIdAndUserId(groupId, user.getId());
        groupInviteRepository.delete(invite);
        return group;
    }

    public void declineInvitation(Long userId, Long groupId) {
        GroupInvite invite = groupInviteRepository.findOneByGroupIdAndUserId(groupId, userId);
        groupInviteRepository.delete(invite);
    }
}

