package com.messenger.controller;

import com.messenger.model.dto.group.GroupAddReceiveDTO;
import com.messenger.model.dto.group.GroupAddUserDTO;
import com.messenger.model.dto.group.GroupGivePermission;
import com.messenger.model.dto.group.GroupRemoveUserDTO;
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

}
