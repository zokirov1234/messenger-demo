package com.messenger.service;

import com.messenger.exp.BadRequestException;
import com.messenger.exp.ItemNotFoundException;
import com.messenger.exp.NotPermissionException;
import com.messenger.model.dto.channel.*;
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
@Transactional
public class ChannelUserService {

    private final ChannelRepository channelRepository;

    private final ChannelUserRepository channelUserRepository;

    private final ChatRepository chatRepository;

    private final ChatUserRepository chatUserRepository;

    private final UserRepository userRepository;


    public ChannelAddUserResponseDTO addUser(ChannelAddUserReceiveDTO channelAddUserReceiveDTO) {

        UserEntity user
                = userRepository.findByUsernameForServices(CurrentUserUtil.getCurrentUser());

        ChannelEntity channel = channelRepository.findById(channelAddUserReceiveDTO.getChannelId()).orElseThrow(() -> {
            throw new ItemNotFoundException("Channel not found");
        });

        ChannelUserEntity channelUser = channelUserRepository.findBySubscribersIdAndChannelId(user.getId(), channel.getId());

        if (channelUser.getPermission() != Permission.ADMIN) {
            throw new NotPermissionException("Not allowed");
        }

        ChatEntity chat = chatRepository.findById(channel.getChat().getId()).orElseThrow(() -> {
            throw new ItemNotFoundException("Chat not found");
        });

        int count = 0;
        for (String subscriber : channelAddUserReceiveDTO.getUsernames()) {

            UserEntity usernameForServices
                    = userRepository.findByUsernameForServices(subscriber);

            if (usernameForServices == null) {
                throw new ItemNotFoundException("User not found");
            }

            ChannelUserEntity users
                    = channelUserRepository.findBySubscribersIdAndChannelId(usernameForServices.getId(), channel.getId());

            if (users != null) {
                continue;
            }

            chatUserRepository.save(
                    ChatUserEntity.builder()
                            .user(usernameForServices)
                            .chat(chat)
                            .build()
            );

            channelUserRepository.save(
                    ChannelUserEntity.builder()
                            .permission(Permission.USER)
                            .subscribers(usernameForServices)
                            .channel(channel)
                            .build()
            );
            ++count;
        }
        channelRepository.setNumberOfSubscribers(
                channel.getNumberOfSubscribers() + count, channel.getId()
        );

        return new ChannelAddUserResponseDTO(
                channel.getNumberOfSubscribers() + count, user.getUsername()
        );
    }


    public ChannelGivePermissionResponseDTO givePermission(ChannelGivePermissionReceiveDTO receive) {

        UserEntity newAdmin = userRepository.findByUsernameForServices(receive.getUsername());
        UserEntity owner = userRepository.findByUsernameForServices(CurrentUserUtil.getCurrentUser());

        ChannelUserEntity ownerChannel = channelUserRepository.findBySubscribersIdAndChannelId(owner.getId(), receive.getChannelId());
        if (ownerChannel.getPermission() == Permission.USER) {
            throw new NotPermissionException("Not allowed");
        }

        ChannelUserEntity newChannelAdmin
                = channelUserRepository.findBySubscribersIdAndChannelId(newAdmin.getId(), receive.getChannelId());

        if (newChannelAdmin == null) {
            throw new ItemNotFoundException("User not found");
        }

        channelUserRepository.setPermission(Permission.ADMIN, receive.getChannelId(), newChannelAdmin.getId());

        return new ChannelGivePermissionResponseDTO(
                newAdmin.getUsername(), "Done"
        );
    }

    public String getPermission(ChannelGetPermissionReceiveDTO receiveDTO) {
        //todo: get permission if it is admin;

        UserEntity isAdmin
                = userRepository.findByUsernameForServices(CurrentUserUtil.getCurrentUser());

        UserEntity willSubscriber = userRepository.findByUsernameForServices(receiveDTO.getUsername());

        if (willSubscriber == null) {
            throw new ItemNotFoundException("User not found");
        }

        ChannelEntity channel = channelRepository.findById(receiveDTO.getChannelId()).orElseThrow(() -> {
            throw new ItemNotFoundException("Channel found");
        });

        ChannelUserEntity admin
                = channelUserRepository.findBySubscribersIdAndChannelId(isAdmin.getId(), channel.getId());

        if (admin == null) {
            throw new ItemNotFoundException("User not found");
        }

        ChannelUserEntity subscriber
                = channelUserRepository.findBySubscribersIdAndChannelId(willSubscriber.getId(), channel.getId());

        if (subscriber == null) {
            throw new ItemNotFoundException("User not found");
        }

        if (admin.getPermission() != Permission.ADMIN) {
            throw new NotPermissionException("Not allowed");
        }

        channelUserRepository.setPermission(Permission.USER, channel.getId(), subscriber.getId());


        return "success";
    }
    public String removeUsers(ChannelRemoveUsersDTO channelRemoveUsersDTO) {

        UserEntity owner = userRepository.findByUsernameForServices(CurrentUserUtil.getCurrentUser());

        ChannelEntity channel = channelRepository.findById(channelRemoveUsersDTO.getChannelId()).orElseThrow(() -> {
            throw new ItemNotFoundException("Channel not found");
        });

        ChannelUserEntity admin = channelUserRepository.findBySubscribersIdAndChannelId(owner.getId(), channel.getId());

        if (admin.getPermission() != Permission.ADMIN) {
            throw new NotPermissionException("Not allowed");
        }
        int count = 0;
        for (String username : channelRemoveUsersDTO.getUsernames()) {
            UserEntity subscriber = userRepository.findByUsernameForServices(username);

            if (subscriber == null) {
                throw new ItemNotFoundException("User not found");
            }
            Optional<ChatUserEntity> chatuser = chatUserRepository.findByChatIdAndUserId(channel.getChat().getId(), subscriber.getId());

            if (chatuser.isEmpty()) {
                continue;
            }
            chatUserRepository.deleteById(chatuser.get().getId());

            ChannelUserEntity channelUser = channelUserRepository.findBySubscribersIdAndChannelId(subscriber.getId(), channel.getId());
            channelUserRepository.deleteById(channelUser.getId());

            ++count;
        }
        int numberOfSubscribers = channel.getNumberOfSubscribers() - count;
        channelRepository.setNumberOfSubscribers(numberOfSubscribers, channel.getId());

        return "success";
    }
}
