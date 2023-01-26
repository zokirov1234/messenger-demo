package com.messenger.service;

import com.messenger.exp.BadRequestException;
import com.messenger.model.dto.chat.ChatUserDTO;
import com.messenger.model.entity.ChatUserEntity;
import com.messenger.model.entity.UserEntity;
import com.messenger.repository.ChatUserRepository;
import com.messenger.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ChatService {

    private final ChatUserRepository chatUserRepository;

    private final UserRepository userRepository;

    public List<ChatUserDTO> getChatOfUser(String username) {

        UserEntity user = userRepository.findByUsernameForServices(username);


        List<ChatUserDTO> chats = new ArrayList<>();
        List<ChatUserEntity> friendsByChatId = chatUserRepository.getFriendsByChatId(user.getId());

        for (ChatUserEntity chatUser : friendsByChatId) {
            if (!chatUser.getUser().isDeleted()) {
                chats.add(
                        new ChatUserDTO(
                                chatUser.getUser().getName(),
                                chatUser.getUser().getId(),
                                chatUser.getChat().getId()
                        )
                );
            }
        }


        return chats;
    }
}
