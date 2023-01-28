package com.messenger.service;

import com.messenger.exp.BadRequestException;
import com.messenger.exp.ItemNotFoundException;
import com.messenger.exp.NotPermissionException;
import com.messenger.model.dto.group.GroupAddReceiveDTO;
import com.messenger.model.dto.group.GroupAddUserDTO;
import com.messenger.model.entity.*;
import com.messenger.model.enums.Permission;
import com.messenger.model.enums.Types;
import com.messenger.repository.*;
import com.messenger.util.CurrentUserUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class GroupService {

    private final GroupRepository groupRepository;

    private final GroupUserRepository groupUserRepository;

    private final ChatUserRepository chatUserRepository;

    private final ChatRepository chatRepository;

    private final UserRepository userRepository;

    private final ChatTypeService chatTypeService;

    private final MessageRepository messageRepository;

    //todo: create group and add at least one user
    //todo: adding group friends
    //todo: user left group


    public String createGroup(GroupAddReceiveDTO groupAddReceiveDTO, String ownerUsername) {
        UserEntity owner = userRepository.findByUsernameForServices(ownerUsername);

        if (groupAddReceiveDTO.getUsernames().isEmpty()) {
            throw new ItemNotFoundException("Users not found");
        }
        ChatEntity chat = new ChatEntity();

        GroupEntity group = groupRepository.save(
                GroupEntity.builder()
                        .chat(chat)
                        .numberOfUsers(groupAddReceiveDTO.getUsernames().size())
                        .name(groupAddReceiveDTO.getName())
                        .description(groupAddReceiveDTO.getDescription())
                        .chatTypes(chatTypeService.createType("a", Types.PRIVATE.name()))
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


    public String addToGroup(GroupAddUserDTO groupAddUserDTO) {

        GroupEntity group = groupRepository.findById(groupAddUserDTO.getGroupId()).orElseThrow(() -> {
            throw new ItemNotFoundException("Group not found");
        });


        ChatEntity chat = chatRepository.findById(groupAddUserDTO.getChatId()).orElseThrow(() -> {
            throw new ItemNotFoundException("Chat not found");
        });

        if (group.getChat().getId() != chat.getId()) {
            throw new ItemNotFoundException("Group not found");
        }


        for (String friends : groupAddUserDTO.getUsernames()) {
            UserEntity friend = userRepository.findByUsernameForServices(friends);

            if (friend == null) {
                throw new ItemNotFoundException("User not found");
            }

            Optional<GroupUserEntity> check = groupUserRepository.findByGroupIdAndParticipantsId(group.getId(), friend.getId());

            if (check.isPresent()) {
                continue;
            }

            chatUserRepository.save(
                    ChatUserEntity.builder()
                            .chat(chat)
                            .user(friend)
                            .build()
            );

            groupUserRepository.save(
                    GroupUserEntity.builder()
                            .participants(friend)
                            .permission(Permission.USER)
                            .group(group)
                            .build()
            );
        }
        int numberOfSubscribers = group.getNumberOfUsers() + groupAddUserDTO.getUsernames().size();
        groupRepository.setNumberOfUsers(numberOfSubscribers, group.getId());

        return "Added successfully";
    }


    public String leaveGroup(int chatId, String username) {

        UserEntity user = userRepository.findByUsernameForServices(username);

        ChatEntity chat = chatRepository.findById(chatId).orElseThrow(() -> {
            throw new ItemNotFoundException("Chat not found");
        });

        GroupEntity group = groupRepository.findByChatId(chatId).orElseThrow(() -> {
            throw new ItemNotFoundException("Group not found");
        });

        int numberOfSubscribers = group.getNumberOfUsers() - 1;
        groupRepository.setNumberOfUsers(numberOfSubscribers, group.getId());
        chatUserRepository.deleteByUserIdAndChatId(user.getId(), chat.getId());
        groupUserRepository.deleteByGroupIdAndUserId(group.getId(), user.getId());

        return "success";
    }

    public String deleteGroup(int groupId) {


        UserEntity owner = userRepository.findByUsernameForServices(CurrentUserUtil.getCurrentUser());

        if (owner == null) {
            throw new ItemNotFoundException("User not found");
        }

        GroupEntity group = groupRepository.findById(groupId).orElseThrow(() -> {
            throw new ItemNotFoundException("Group not found");
        });

        GroupUserEntity admin = groupUserRepository.findByGroupIdAndParticipantsId(groupId, owner.getId()).orElseThrow(() -> {
            throw new ItemNotFoundException("User not found");
        });

        if (admin.getPermission() != Permission.ADMIN) {
            throw new NotPermissionException("Not allowed");
        }

        groupUserRepository.deleteByGroupId(groupId);
        chatUserRepository.deleteByChatId(group.getChat().getId());
        chatRepository.deleteById(group.getChat().getId());
        messageRepository.deleteByChatId(group.getChat().getId());
        groupRepository.deleteById(groupId);

        return "success";
    }

    public String deleteMessage(int messageId) {

        UserEntity user
                = userRepository.findByUsernameForServices(CurrentUserUtil.getCurrentUser());

        MessageEntity message = messageRepository.findById(messageId).orElseThrow(() -> {
            throw new ItemNotFoundException("Message not found");
        });

        if (message.getSenderId() != user.getId()) {
            throw new NotPermissionException("Not allowed");
        }

        messageRepository.deleteById(messageId);

        return "success";
    }
}
