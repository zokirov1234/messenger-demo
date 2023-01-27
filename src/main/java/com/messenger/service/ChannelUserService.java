package com.messenger.service;

import com.messenger.exp.BadRequestException;
import com.messenger.model.dto.channel.ChannelAddUserReceiveDTO;
import com.messenger.model.dto.channel.ChannelAddUserResponseDTO;
import com.messenger.model.dto.channel.ChannelGivePermissionReceiveDTO;
import com.messenger.model.dto.channel.ChannelGivePermissionResponseDTO;
import com.messenger.model.entity.*;
import com.messenger.model.enums.Permission;
import com.messenger.repository.*;
import com.messenger.util.CurrentUserUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ChannelUserService {

    private final ChannelRepository channelRepository;

    private final ChannelUserRepository channelUserRepository;

    private final ChatRepository chatRepository;

    private final ChatUserRepository chatUserRepository;

    private final UserRepository userRepository;

    @Transactional
    public ChannelAddUserResponseDTO addUser(ChannelAddUserReceiveDTO channelAddUserReceiveDTO) {

        UserEntity user
                = userRepository.findByUsernameForServices(CurrentUserUtil.getCurrentUser());

        Optional<ChannelEntity> channel = channelRepository.findById(channelAddUserReceiveDTO.getChannelId());

        if (channel.isEmpty()) {
            throw new BadRequestException("Channel not found");
        }

        ChannelUserEntity channelUser = channelUserRepository.findBySubscribersId(user.getId(), channel.get().getId());

        if (channelUser.getPermission() != Permission.ADMIN) {
            throw new BadRequestException("Not allowed");
        }

        Optional<ChatEntity> chat = chatRepository.findById(channel.get().getChat().getId());

        if (chat.isEmpty()) {
            throw new BadRequestException("Chat not found");
        }

        int count = 0;
        for (String subscriber : channelAddUserReceiveDTO.getUsernames()) {

            UserEntity usernameForServices
                    = userRepository.findByUsernameForServices(subscriber);

            if (usernameForServices == null) {
                throw new BadRequestException("User not found");
            }

            chatUserRepository.save(
                    ChatUserEntity.builder()
                            .user(usernameForServices)
                            .chat(chat.get())
                            .build()
            );

            channelUserRepository.save(
                    ChannelUserEntity.builder()
                            .permission(Permission.USER)
                            .subscribers(usernameForServices)
                            .channel(channel.get())
                            .build()
            );
            ++count;
        }
        channelRepository.setNumberOfSubscribers(
                channel.get().getNumberOfSubscribers() + count, channel.get().getId()
        );

        return new ChannelAddUserResponseDTO(
                channel.get().getNumberOfSubscribers() + count, user.getUsername()
        );
    }

    @Transactional
    public ChannelGivePermissionResponseDTO givePermission(ChannelGivePermissionReceiveDTO receive) {

        UserEntity newAdmin = userRepository.findByUsernameForServices(receive.getUsername());
        UserEntity owner = userRepository.findByUsernameForServices(CurrentUserUtil.getCurrentUser());

        ChannelUserEntity ownerChannel = channelUserRepository.findBySubscribersId(owner.getId(), receive.getChannelId());
        if (ownerChannel.getPermission() == Permission.USER) {
            throw new BadRequestException("Not allowed");
        }

        ChannelUserEntity newChannelAdmin
                = channelUserRepository.findBySubscribersId(newAdmin.getId(), receive.getChannelId());

        if (newChannelAdmin == null) {
            throw new BadRequestException("Not found");
        }

        channelUserRepository.setAsAdmin(Permission.ADMIN, receive.getChannelId(), newChannelAdmin.getId());

        return new ChannelGivePermissionResponseDTO(
                newAdmin.getUsername(), "Done"
        );
    }
}
