package com.messenger.controller;

import com.messenger.model.dto.channel.*;
import com.messenger.service.ChannelService;
import com.messenger.service.ChannelUserService;
import com.messenger.service.ProfilePhotoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/channel")
@AllArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    private final ProfilePhotoService profilePhotoService;

    private final ChannelUserService channelUserService;

    @PostMapping("/create")
    public ResponseEntity<ChannelCreatedResponse> createChannel(
            @RequestBody @Valid ChannelCreatedRequest channelCreatedRequest
    ) {
        ChannelCreatedResponse channelCreatedResponse = channelService.createChannel(channelCreatedRequest);

        return ResponseEntity.ok().body(channelCreatedResponse);
    }

    @PostMapping("/broadcast")
    public ResponseEntity<ChannelBroadCastResponseDTO> broadCast(
            @RequestBody @Valid ChannelBroadCastReceiveDTO channelBroadCastReceiveDTO
    ) {

        ChannelBroadCastResponseDTO responseDTO = channelService.broadCast(channelBroadCastReceiveDTO);

        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("/add/user")
    public ResponseEntity<ChannelAddUserResponseDTO> addUser(
            @RequestBody @Valid ChannelAddUserReceiveDTO channelAddUserReceiveDTO
    ) {

        ChannelAddUserResponseDTO channelAddUserResponseDTO
                = channelUserService.addUser(channelAddUserReceiveDTO);

        return ResponseEntity.ok().body(channelAddUserResponseDTO);
    }

    @PostMapping("/give/permission")
    public ResponseEntity<ChannelGivePermissionResponseDTO> givePermission(
            @RequestBody @Valid ChannelGivePermissionReceiveDTO channelGivePermissionReceiveDTO
    ) {

        ChannelGivePermissionResponseDTO channelGivePermissionResponseDTO
                = channelUserService.givePermission(channelGivePermissionReceiveDTO);

        return ResponseEntity.ok().body(channelGivePermissionResponseDTO);
    }

    @PostMapping("/get/permission")
    public ResponseEntity<?> getPermission(
            @RequestBody @Valid ChannelGetPermissionReceiveDTO channelGetPermissionReceiveDTO
    ) {

        String response
                = channelUserService.getPermission(channelGetPermissionReceiveDTO);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/remove/users")
    public ResponseEntity<?> removeUsers(
            @RequestBody @Valid ChannelRemoveUsersDTO channelRemoveUsersDTO
    ) {

        String response = channelUserService.removeUsers(channelRemoveUsersDTO);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{channelId}")
    public ResponseEntity<?> deleteChannel(
            @PathVariable("channelId") int channelId
    ) {

        String response = channelService.deleteChannel(channelId);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{channelId}/message/{messageId}")
    public ResponseEntity<?> deleteMessage(
            @PathVariable("channelId") int channelId,
            @PathVariable("messageId") int messageId
    ) {

        String response = channelService.deleteMessage(channelId, messageId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/set/photo{channelId}")
    public ResponseEntity<?> setPhotoOnChannelProfile(
            @PathVariable("channelId") Integer channelId,
            @RequestParam("file")MultipartFile file
            ) {
        String response
                = profilePhotoService.setPhotoOnChannelProfile(file,channelId);

        return ResponseEntity.ok(response);
    }


}
