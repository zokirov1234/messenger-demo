package com.messenger.service;

import com.messenger.exp.BadRequestException;
import com.messenger.model.dto.channel.*;
import com.messenger.model.entity.*;
import com.messenger.model.enums.Permission;
import com.messenger.repository.*;
import com.messenger.util.CurrentUserUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ChannelService {

    private final UserRepository userRepository;

    private final ChannelRepository channelRepository;

    private final ChatUserRepository chatUserRepository;

    private final MessageRepository messageRepository;

    private final ChannelUserRepository channelUserRepository;
    private final ChatTypeService chatTypeService;

    public ChannelCreatedResponse createChannel(ChannelCreatedRequest channelCreatedRequest) {

        UserEntity owner = userRepository.findByUsernameForServices(CurrentUserUtil.getCurrentUser());

        Optional<ChannelEntity> channel = channelRepository.findByName(channelCreatedRequest.getChannelName());

        if (channel.isPresent()) {
            throw new BadRequestException("Channel already exists");
        }

        ChatEntity chatEntity = new ChatEntity();
        ChannelEntity channel1 = channelRepository.save(
                ChannelEntity.builder()
                        .name(channelCreatedRequest.getChannelName())
                        .description(channelCreatedRequest.getDescription())
                        .chatTypes(chatTypeService.createType(channelCreatedRequest.getChatLink(), channelCreatedRequest.getChannelType()))
                        .numberOfSubscribers(1)
                        .chat(chatEntity)
                        .build()
        );

        channelUserRepository.save(
                ChannelUserEntity.builder()
                        .channel(channel1)
                        .subscribers(owner)
                        .permission(Permission.ADMIN)
                        .build()
        );

        chatUserRepository.save(
                ChatUserEntity.builder()
                        .chat(chatEntity)
                        .user(owner)
                        .build()
        );



        return new ChannelCreatedResponse(
                channel1.getId(),
                channel1.getName(), 1, channel1.getDescription(),
                channel1.getChatTypes().getLink(), List.of(owner.getUsername())
        );
    }

    public ChannelBroadCastResponseDTO broadCast(ChannelBroadCastReceiveDTO channelBroadCastReceiveDTO) {

        UserEntity admin = userRepository.findByUsernameForServices(CurrentUserUtil.getCurrentUser());

        Optional<ChannelEntity> channel
                = channelRepository.findById(channelBroadCastReceiveDTO.getChannelId());

        if (channel.isEmpty()) {
            throw new BadRequestException("Channel not found");
        }

        ChannelUserEntity channelUser = channelUserRepository.findBySubscribersId(admin.getId(), channel.get().getId());

        if (channelUser == null) {
            throw new BadRequestException("Not found");
        }

        if (channelUser.getPermission() == Permission.USER) {
            throw new BadRequestException("Not allowed");
        }

        MessageEntity message = messageRepository.save(
                MessageEntity.builder()
                        .senderId(admin.getId())
                        .message(channelBroadCastReceiveDTO.getMessage())
                        .chat(channel.get().getChat())
                        .build()
        );

        return new ChannelBroadCastResponseDTO(
                message.getMessage(), message.getCreatedAt()
        );
    }
}
