package com.messenger.controller;

import com.messenger.model.dto.user.UserFindReceive;
import com.messenger.model.dto.user.UserResponseDTO;
import com.messenger.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
