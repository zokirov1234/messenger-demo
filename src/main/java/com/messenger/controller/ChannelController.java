package com.messenger.controller;

import com.messenger.model.dto.channel.*;
import com.messenger.service.ChannelService;
import com.messenger.service.ChannelUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/channel")
@AllArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

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

}
