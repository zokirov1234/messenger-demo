package com.messenger.controller;

import com.messenger.model.dto.chat.ChatResponseListDTO;
import com.messenger.model.dto.user.UserFindReceive;
import com.messenger.model.dto.user.UserResponseDTO;
import com.messenger.model.dto.user.UserUpdateProfile;
import com.messenger.service.ProfilePhotoService;
import com.messenger.service.UserService;
import com.messenger.util.CurrentUserUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    private final ProfilePhotoService profilePhotoService;

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
            @RequestBody @Valid UserUpdateProfile userUpdateProfile
    ) {
        String response = userService.editProfile(userUpdateProfile, CurrentUserUtil.getCurrentUser());


        return ResponseEntity.ok(response);
    }

    @GetMapping("/delete")
    public ResponseEntity<?> deleteProfile() {
        String response = userService.deleteProfile(CurrentUserUtil.getCurrentUser());

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/set/photo")
    public ResponseEntity<?> setPhotoOnProfile(
            @RequestParam("file") MultipartFile file
    ) {
        String response
                = profilePhotoService.setPhotoOnUser(file);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAll/chats")
    public ResponseEntity<List<ChatResponseListDTO>> getAllChats() {
        
        List<ChatResponseListDTO> responseListDTOS = userService.getAllChats();

        return ResponseEntity.ok().body(responseListDTOS);
    }

    @GetMapping("/get/private/chats")
    public ResponseEntity<List<ChatResponseListDTO>> getPrivateChats() {

        List<ChatResponseListDTO> responseListDTOS = userService.getAllPrivateChats();

        return ResponseEntity.ok().body(responseListDTOS);
    }

}
