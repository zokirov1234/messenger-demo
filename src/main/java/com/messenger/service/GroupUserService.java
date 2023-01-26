package com.messenger.service;

import com.messenger.exp.BadRequestException;
import com.messenger.model.dto.group.GroupGivePermission;
import com.messenger.model.dto.group.GroupRemoveUserDTO;
import com.messenger.model.entity.ChatEntity;
import com.messenger.model.entity.GroupEntity;
import com.messenger.model.entity.GroupUserEntity;
import com.messenger.model.entity.UserEntity;
import com.messenger.model.enums.Permission;
import com.messenger.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GroupUserService {

    private final GroupRepository groupRepository;

    private final GroupUserRepository groupUserRepository;

    private final ChatUserRepository chatUserRepository;

    private final ChatRepository chatRepository;

    private final UserRepository userRepository;

    //todo: remove users from group is admin
    //todo: give permission if admin
    @Transactional
    public String removeUsers(GroupRemoveUserDTO groupRemoveUserDTO, String username) {

        UserEntity user = userRepository.findByUsernameForServices(username);

        Optional<ChatEntity> chat = chatRepository.findById(groupRemoveUserDTO.getChatId());

        if (chat.isEmpty()) {
            throw new BadRequestException("Chat not found");
        }

        Optional<GroupEntity> group = groupRepository.findByChatId(chat.get().getId());

        if (group.isEmpty()) {
            throw new BadRequestException("Group not found");
        }

        Optional<GroupUserEntity> groupUser
                = groupUserRepository.findByGroupIdAndParticipantsId(group.get().getId(), user.getId());

        if (groupUser.isEmpty()) {
            throw new BadRequestException("Not subscribed");
        }

        if (groupUser.get().getPermission() == Permission.USER) {
            throw new BadRequestException("Not allowed");
        }

        for (String usernames : groupRemoveUserDTO.getUsernames()) {
            Optional<UserEntity> subscribers = userRepository.findByUsername(usernames);
            if (subscribers.isEmpty()) {
                throw new BadRequestException("Not subscribed user");
            }
            int groupUsersNumbers = group.get().getNumberOfUsers() - 1;
            groupRepository.setNumberOfUsers(groupUsersNumbers, group.get().getId());
            chatUserRepository.deleteByUserIdAndChatId(subscribers.get().getId(), chat.get().getId());
            groupUserRepository.deleteByGroupIdAndUserId(group.get().getId(), subscribers.get().getId());
        }

        return "All users removed";
    }

    @Transactional
    public String givePermission(GroupGivePermission givePermission, String owner) {
        Optional<GroupEntity> group = groupRepository.findById(givePermission.getGroupId());

        if (group.isEmpty()) {
            throw new BadRequestException("Group not found");
        }

        Optional<UserEntity> admin = userRepository.findByUsername(owner);

        Optional<GroupUserEntity> groupAdmin = groupUserRepository.findByGroupIdAndParticipantsId(group.get().getId(), admin.get().getId());

        if (groupAdmin.get().getPermission() == Permission.USER) {
            throw new BadRequestException("Not allowed");
        }

        Optional<UserEntity> subscriber = userRepository.findByUsername(givePermission.getUsername());
        if (subscriber.isEmpty()) {
            throw new BadRequestException("User not found");
        }

        groupUserRepository.setAsAdmin(Permission.ADMIN, group.get().getId(), subscriber.get().getId());

        return "Updated";
    }
}
