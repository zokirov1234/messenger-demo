package com.messenger.service;

import com.messenger.exp.BadRequestException;
import com.messenger.model.dto.message.MessageListDTO;
import com.messenger.model.dto.message.MessageSendDTO;
import com.messenger.model.entity.ChatEntity;
import com.messenger.model.entity.ChatUserEntity;
import com.messenger.model.entity.MessageEntity;
import com.messenger.model.entity.UserEntity;
import com.messenger.repository.ChatRepository;
import com.messenger.repository.ChatUserRepository;
import com.messenger.repository.MessageRepository;
import com.messenger.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    private final ChatUserRepository chatUserRepository;
    private final ChatRepository chatRepository;

    public MessageListDTO getAllMessages(int chatId, String senderUsername) {

        List<MessageEntity> messages = messageRepository.getAllByChatId(chatId);

        Optional<UserEntity> user = userRepository.findByUsername(senderUsername);

        if (user.isEmpty()) {
            throw new BadRequestException("User not found");
        }

        MessageListDTO messageListDTOS = new MessageListDTO();
        for (MessageEntity message : messages) {
            if (message.getUser().getId() == user.get().getId()) {
                messageListDTOS.setOwnerMessage(List.of(message.getMessage()));
            } else {
                messageListDTOS.setFriendsMessage(List.of(message.getMessage()));
            }
        }

        return messageListDTOS;
    }

    public String sendMessage(MessageSendDTO messageSendDTO, String username) {

        Optional<UserEntity> owner = userRepository.findByUsername(username);

        if (owner.isEmpty()) {
            throw new BadRequestException("User not found");
        }

        Optional<UserEntity> friend = userRepository.findById(messageSendDTO.getFriendId());

        if (friend.isEmpty()) {
            throw new BadRequestException("User not found");
        }

        Optional<ChatEntity> chat = chatRepository.findById(messageSendDTO.getChatId());
        ChatEntity chatEntity;
        if (chat.isEmpty()) {
            ChatUserEntity chatUser = chatUserRepository.save(
                    ChatUserEntity.builder()
                            .chat(new ChatEntity())
                            .user(owner.get())
                            .build()
            );

            chatUserRepository.save(
                    ChatUserEntity.builder()
                            .chat(chatUser.getChat())
                            .user(friend.get())
                            .build()
            );
            chatEntity = chatUser.getChat();
        } else {
            chatEntity = chat.get();
        }

        messageRepository.save(
                MessageEntity.builder()
                        .user(owner.get())
                        .message(messageSendDTO.getMessage())
                        .chat(chatEntity)
                        .build()
        );

        return "Sent";
    }

}
