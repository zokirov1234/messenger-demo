package com.messenger.controller;

import com.messenger.model.dto.group.*;
import com.messenger.service.GroupService;
import com.messenger.service.GroupUserService;
import com.messenger.util.CurrentUserUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/group")
@AllArgsConstructor
public class GroupController {

    private final GroupService groupService;

    private final GroupUserService groupUserService;

    @PostMapping("/create")
    public ResponseEntity<?> createGroup(
            @RequestBody @Valid GroupAddReceiveDTO groupAddReceiveDTO
    ) {

        String response = groupService.createGroup(groupAddReceiveDTO, CurrentUserUtil.getCurrentUser());

        return ResponseEntity.ok(response);
    }


    @PostMapping("/add/user")
    public ResponseEntity<?> addGroupUser(
            @RequestBody @Valid GroupAddUserDTO groupAddUserDTO
    ) {

        String response = groupService.addToGroup(groupAddUserDTO);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/leave/{chatId}")
    public ResponseEntity<?> leaveGroup(
            @PathVariable("chatId") int chatId
    ) {

        String response = groupService.leaveGroup(chatId, CurrentUserUtil.getCurrentUser());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/remove/users")
    public ResponseEntity<?> removeUsersFromGroup(
            @RequestBody @Valid GroupRemoveUserDTO groupRemoveUserDTO
            ) {

        String response = groupUserService.removeUsers(groupRemoveUserDTO, CurrentUserUtil.getCurrentUser());

        return ResponseEntity.ok(response);
    }


    @PostMapping("/give/permission")
    public ResponseEntity<?> givePermission(
            @RequestBody @Valid GroupGivePermission givePermission
    ) {
        String response = groupUserService.givePermission(givePermission,CurrentUserUtil.getCurrentUser());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/get/permission")
    public ResponseEntity<?> getPermission(
            @RequestBody @Valid GroupGetPermissionReceiveDTO groupGetPermissionReceiveDTO
    ) {

        String responseDTO
                = groupUserService.getPermission(groupGetPermissionReceiveDTO);

        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping("/delete/{groupId}")
    public ResponseEntity<?> deleteGroup(
            @PathVariable("groupId") int groupId
    ) {

        String response = groupService.deleteGroup(groupId);

        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/delete/message/{messageId}")
    public ResponseEntity<?> deleteMessage(
            @PathVariable("messageId") int messageId
    ) {

        String response = groupService.deleteMessage(messageId);

        return ResponseEntity.ok(response);
    }
}
