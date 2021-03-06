package org.savvas.milked.controller;

import org.savvas.milked.controller.error.ValidationException;
import org.savvas.milked.controller.request.EmailBodyRequest;
import org.savvas.milked.controller.request.UpdateUserRequest;
import org.savvas.milked.domain.MilkedUser;
import org.savvas.milked.domain.MilkingGroup;
import org.savvas.milked.service.UpdateUserService;
import org.savvas.milked.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {

    private final UserService userService;
    private final UpdateUserService updateUserService;

    @Autowired
    public UserController(UserService userService, UpdateUserService updateUserService) {
        this.userService = userService;
        this.updateUserService = updateUserService;
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public MilkedUser getUser(@PathVariable("id") Long id) {
        return userService.getUser(id);
    }

    @RequestMapping(value = "/user/{id}/group", method = RequestMethod.GET)
    public List<MilkingGroup> getUserGroups(@PathVariable("id") Long id) {
        return userService.getUserGroups(id);
    }

    @RequestMapping(value = "/group/{groupId}/users", method = RequestMethod.GET)
    public List<MilkedUser> getGroupUsers(@PathVariable("groupId") Long groupId) {
        List<MilkedUser> groupUserList = userService.getGroupUsers(groupId);
        return groupUserList;
    }

    @RequestMapping(value = "/user/{userId}/group/{groupId}/leave", method = RequestMethod.POST)
    public ResponseEntity leaveGroup(@PathVariable("userId") Long userId, @PathVariable("groupId") Long groupId) {
        userService.leaveGroup(userId, groupId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/user/{uuid}/update", method = RequestMethod.POST)
    public ResponseEntity<MilkedUser> updateUserDetails(@PathVariable("uuid") String uuid, @RequestBody UpdateUserRequest updateUserRequest) {
        if (updateUserRequest.getName().isEmpty() || updateUserRequest.getPassword().isEmpty()) {
            throw new ValidationException("Inval    id name or password");
        }
        MilkedUser updatedUser = updateUserService.updateUserDetails(uuid, updateUserRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @RequestMapping(value = "/user/forgot-password", method = RequestMethod.POST)
    public String recoverPassword(@RequestBody @Valid EmailBodyRequest request) {
        return userService.recoverPassword(request.getEmail());
    }
//    @RequestMapping(value = "/user/{email}/change-password/{password}", method = RequestMethod.POST)
//    public ResponseEntity changePassword(@PathVariable("email") @Email String email, @PathVariable("password") String password){
//        if (email.isEmpty()) {
//            throw new ValidationException("Invalid Email");
//        }
//        userService.changePassword(email, password);
//        return ResponseEntity.ok().build();
//    }
}
