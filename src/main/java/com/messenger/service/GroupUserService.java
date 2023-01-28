package com.messenger.service;

import com.messenger.exp.BadRequestException;
import com.messenger.exp.ItemNotFoundException;
import com.messenger.exp.NotPermissionException;
import com.messenger.model.dto.group.GroupGetPermissionReceiveDTO;
import com.messenger.model.dto.group.GroupGivePermission;
import com.messenger.model.dto.group.GroupRemoveUserDTO;
import com.messenger.model.entity.ChatEntity;
import com.messenger.model.entity.GroupEntity;
import com.messenger.model.entity.GroupUserEntity;
import com.messenger.model.entity.UserEntity;
import com.messenger.model.enums.Permission;
import com.messenger.repository.*;
import com.messenger.util.CurrentUserUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class GroupUserService {

    private final GroupRepository groupRepository;

    private final GroupUserRepository groupUserRepository;

    private final ChatUserRepository chatUserRepository;

    private final ChatRepository chatRepository;

    private final UserRepository userRepository;


    @Transactional
    public String removeUsers(GroupRemoveUserDTO groupRemoveUserDTO, String username) {
        //todo: remove users from group is admin;

        UserEntity user = userRepository.findByUsernameForServices(username);

        ChatEntity chat = chatRepository.findById(groupRemoveUserDTO.getChatId()).orElseThrow(() -> {
            throw new ItemNotFoundException("Chat not found");
        });

        GroupEntity group = groupRepository.findByChatId(chat.getId()).orElseThrow(() -> {
            throw new ItemNotFoundException("Group not found");
        });

        GroupUserEntity groupUser
                = groupUserRepository.findByGroupIdAndParticipantsId(group.getId(), user.getId()).orElseThrow(() -> {
            throw new BadRequestException("Not subscribed");
        });

        if (groupUser.getPermission() == Permission.USER) {
            throw new NotPermissionException("Not allowed");
        }

        for (String usernames : groupRemoveUserDTO.getUsernames()) {
            UserEntity subscribers = userRepository.findByUsernameForServices(usernames);
            if (subscribers == null) {
                throw new ItemNotFoundException("Not subscribed user");
            }
            int groupUsersNumbers = group.getNumberOfUsers() - 1;
            groupRepository.setNumberOfUsers(groupUsersNumbers, group.getId());
            chatUserRepository.deleteByUserIdAndChatId(subscribers.getId(), chat.getId());
            groupUserRepository.deleteByGroupIdAndUserId(group.getId(), subscribers.getId());
        }

        return "success";
    }

    @Transactional
    public String givePermission(GroupGivePermission givePermission, String owner) {
        //todo: give permission if it is admin;

        GroupEntity group = groupRepository.findById(givePermission.getGroupId()).orElseThrow(() -> {
            throw new BadRequestException("Group not found");
        });

        UserEntity admin = userRepository.findByUsernameForServices(owner);

        GroupUserEntity groupAdmin = groupUserRepository.findByGroupIdAndParticipantsId(group.getId(), admin.getId()).orElseThrow(() -> {
            throw new ItemNotFoundException("User not found");
        });

        if (groupAdmin.getPermission() == Permission.USER) {
            throw new NotPermissionException("Not allowed");
        }

        UserEntity subscriber = userRepository.findByUsernameForServices(givePermission.getUsername());
        if (subscriber == null) {
            throw new ItemNotFoundException("User not found");
        }

        groupUserRepository.setPermission(Permission.ADMIN, group.getId(), subscriber.getId());

        return "success";
    }

    public String getPermission(GroupGetPermissionReceiveDTO receiveDTO) {
        //todo : get permission from user if it is admin;

        UserEntity isAdmin = userRepository.findByUsernameForServices(CurrentUserUtil.getCurrentUser());

        UserEntity willSubscriber = userRepository.findByUsernameForServices(receiveDTO.getUsername());

        if (willSubscriber == null) {
            throw new BadRequestException("Subscriber not found");
        }

        GroupEntity group = groupRepository.findById(receiveDTO.getGroupId()).orElseThrow(() -> {
            throw new ItemNotFoundException("Group not found");
        });

        GroupUserEntity groupUser
                = groupUserRepository.findByGroupIdAndParticipantsId(receiveDTO.getGroupId(), isAdmin.getId()).orElseThrow(() -> {
            throw new ItemNotFoundException("User not found");
        });

        GroupUserEntity groupUser1
                = groupUserRepository.findByGroupIdAndParticipantsId(receiveDTO.getGroupId(), willSubscriber.getId()).orElseThrow(() -> {
            throw new ItemNotFoundException("User not found");
        });

        if (groupUser.getPermission() != Permission.ADMIN) {
            throw new NotPermissionException("Not allowed");
        }

        groupUserRepository.setPermission(Permission.USER, receiveDTO.getGroupId(), willSubscriber.getId());

        return "success";
    }
}
