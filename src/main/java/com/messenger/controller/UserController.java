package com.messenger.controller;

import com.messenger.model.dto.user.UserFindReceive;
import com.messenger.model.dto.user.UserResponseDTO;
import com.messenger.model.dto.user.UserUpdateProfile;
import com.messenger.service.UserService;
import com.messenger.util.CurrentUserUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/findAll")
    public ResponseEntity<List<UserResponseDTO>> findAllUsers() {
        List<UserResponseDTO> allUser = userService.getAllUser();
        return ResponseEntity.ok().body(allUser);
    }

    @GetMapping("/find")
    public ResponseEntity<List<UserResponseDTO>> findByUsername(
            @RequestBody UserFindReceive username
    ) {
        List<UserResponseDTO> users = userService.findByUsername(username.getUsername());

        return ResponseEntity.ok().body(users);
    }

    @PostMapping("/edit")
    public ResponseEntity<?> editProfile(
            @RequestBody UserUpdateProfile userUpdateProfile
    ) {
        String response = userService.editProfile(userUpdateProfile, CurrentUserUtil.getCurrentUser());


        return ResponseEntity.ok(response);
    }

    @GetMapping("/delete")
    public ResponseEntity<?> deleteProfile() {
        String response = userService.deleteProfile(CurrentUserUtil.getCurrentUser());

        return ResponseEntity.ok().body(response);
    }

}
