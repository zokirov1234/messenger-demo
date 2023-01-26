package com.messenger.service;

import com.messenger.exp.BadRequestException;
import com.messenger.model.dto.group.GroupAddReceiveDTO;
import com.messenger.model.dto.group.GroupAddUserDTO;
import com.messenger.model.dto.group.GroupGivePermission;
import com.messenger.model.dto.group.GroupRemoveUserDTO;
import com.messenger.model.entity.*;
import com.messenger.model.enums.Permission;
import com.messenger.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    private final GroupUserRepository groupUserRepository;

    private final ChatUserRepository chatUserRepository;

    private final ChatRepository chatRepository;

    private final UserRepository userRepository;

    //todo: create group and add at least one user
    //todo: adding group friends
    //todo: user left group


    @Transactional
    public String createGroup(GroupAddReceiveDTO groupAddReceiveDTO, String ownerUsername) {
        UserEntity owner = userRepository.findByUsernameForServices(ownerUsername);

        if (groupAddReceiveDTO.getUsernames().isEmpty()) {
            throw new BadRequestException("Users not found");
        }
        ChatEntity chat = new ChatEntity();

        GroupEntity group = groupRepository.save(
                GroupEntity.builder()
                        .chat(chat)
                        .numberOfUsers(groupAddReceiveDTO.getUsernames().size())
                        .name(groupAddReceiveDTO.getName())
                        .description(groupAddReceiveDTO.getDescription())
                        .build()
        );

        for (String username : groupAddReceiveDTO.getUsernames()) {

            UserEntity user = userRepository.findByUsernameForServices(username);
            if (user == owner) {
                continue;
            }

            chatUserRepository.save(
                    ChatUserEntity.builder()
                            .user(user)
                            .chat(chat)
                            .build()
            );
            groupUserRepository.save(
                    GroupUserEntity.builder()
                            .group(group)
                            .permission(Permission.USER)
                            .participants(user)
                            .build()
            );
        }
        chatUserRepository.save(
                ChatUserEntity.builder()
                        .user(owner)
                        .chat(chat)
                        .build()
        );
        groupUserRepository.save(
                GroupUserEntity.builder()
                        .group(group)
                        .permission(Permission.ADMIN)
                        .participants(owner)
                        .build()
        );

        return "Created successfully";
    }

    @Transactional
    public String addToGroup(GroupAddUserDTO groupAddUserDTO, String username) {

        Optional<GroupEntity> group = groupRepository.findById(groupAddUserDTO.getGroupId());

        if (group.isEmpty()) {
            throw new BadRequestException("Group not found");
        }

        Optional<ChatEntity> chat = chatRepository.findById(groupAddUserDTO.getChatId());

        if (chat.isEmpty()) {
            throw new BadRequestException("Chat not found");
        }

        if (group.get().getChat().getId() != chat.get().getId()) {
            throw new BadRequestException("Group not found");
        }


        for (String friends : groupAddUserDTO.getUsernames()) {
            Optional<UserEntity> friend = userRepository.findByUsername(friends);

            if (friend.isEmpty() || friend.get().isDeleted()) {
                throw new BadRequestException("User not found");
            }

            Optional<GroupUserEntity> check = groupUserRepository.findByGroupIdAndParticipantsId(group.get().getId(), friend.get().getId());

            if (check.isPresent()) {
                continue;
            }

            chatUserRepository.save(
                    ChatUserEntity.builder()
                            .chat(chat.get())
                            .user(friend.get())
                            .build()
            );

            groupUserRepository.save(
                    GroupUserEntity.builder()
                            .participants(friend.get())
                            .permission(Permission.USER)
                            .group(group.get())
                            .build()
            );
        }
        int numberOfSubscribers = group.get().getNumberOfUsers() + groupAddUserDTO.getUsernames().size();
        groupRepository.setNumberOfUsers(numberOfSubscribers, group.get().getId());

        return "Added successfully";
    }

    @Transactional
    public String leaveGroup(int chatId, String username) {

        UserEntity user = userRepository.findByUsernameForServices(username);

        Optional<ChatEntity> chat = chatRepository.findById(chatId);

        if (chat.isEmpty()) {
            throw new BadRequestException("Chat not found");
        }

        Optional<GroupEntity> group = groupRepository.findByChatId(chatId);

        if (group.isEmpty()) {
            throw new BadRequestException("Group not found");
        }
        int numberOfSubscribers = group.get().getNumberOfUsers() - 1;
        groupRepository.setNumberOfUsers(numberOfSubscribers, group.get().getId());
        chatUserRepository.deleteByUserIdAndChatId(user.getId(), chat.get().getId());
        groupUserRepository.deleteByGroupIdAndUserId(group.get().getId(), user.getId());

        return "Left";
    }
}
