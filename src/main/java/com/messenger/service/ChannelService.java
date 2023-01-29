package com.messenger.service;

import com.messenger.exp.BadRequestException;
import com.messenger.exp.ItemNotFoundException;
import com.messenger.exp.NotPermissionException;
import com.messenger.model.dto.channel.*;
import com.messenger.model.entity.*;
import com.messenger.model.enums.Permission;
import com.messenger.model.enums.PinType;
import com.messenger.repository.*;
import com.messenger.util.CurrentUserUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
@Transactional
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

        ChannelEntity channel
                = channelRepository.findById(channelBroadCastReceiveDTO.getChannelId()).orElseThrow(() -> {
                    throw new ItemNotFoundException("Channel not found");
        });

        ChannelUserEntity channelUser = channelUserRepository.findBySubscribersIdAndChannelId(admin.getId(), channel.getId());

        if (channelUser == null) {
            throw new ItemNotFoundException("User not found");
        }

        if (channelUser.getPermission() == Permission.USER) {
            throw new NotPermissionException("Not allowed");
        }

        MessageEntity message = messageRepository.save(
                MessageEntity.builder()
                        .senderId(admin.getId())
                        .message(channelBroadCastReceiveDTO.getMessage())
                        .pinType(PinType.UNPINNED)
                        .chat(channel.getChat())
                        .build()
        );

        return new ChannelBroadCastResponseDTO(
                message.getMessage(), message.getCreatedAt()
        );
    }

    public String deleteChannel(int channelId) {

        UserEntity owner = userRepository.findByUsernameForServices(CurrentUserUtil.getCurrentUser());

        if (owner == null) {
            throw new ItemNotFoundException("User not found");
        }

        ChannelEntity channel = channelRepository.findById(channelId).orElseThrow(() -> {
            throw new ItemNotFoundException("Channel not found");
        });

        ChannelUserEntity admin = channelUserRepository.findBySubscribersIdAndChannelId(owner.getId(), channelId);

        if (admin == null) {
            throw new ItemNotFoundException("User not found");
        }

        if (admin.getPermission() != Permission.ADMIN) {
            throw new NotPermissionException("Not allowed");
        }

        chatUserRepository.deleteByChatId(channel.getChat().getId());
        messageRepository.deleteByChatId(channel.getChat().getId());
        channelUserRepository.deleteByChannelId(channelId);
        channelRepository.deleteById(channelId);

        return "success";
    }

    public String deleteMessage(int messageId, int id) {

        UserEntity isAdmin = userRepository.findByUsernameForServices(CurrentUserUtil.getCurrentUser());

        ChannelEntity channel = channelRepository.findById(id).orElseThrow(() -> {
            throw new ItemNotFoundException("Channel not found");
        });

        ChannelUserEntity isOwner = channelUserRepository.findBySubscribersIdAndChannelId(isAdmin.getId(), channel.getId());

        if (isOwner == null) {
            throw new ItemNotFoundException("User not found");
        }

        if (isOwner.getPermission() != Permission.ADMIN) {
            throw new NotPermissionException("Not allowed");
        }

        MessageEntity message = messageRepository.findById(messageId).orElseThrow(() -> {
            throw new ItemNotFoundException("Message not found");
        });

        messageRepository.deleteById(message.getId());

        return "success";
    }
}
