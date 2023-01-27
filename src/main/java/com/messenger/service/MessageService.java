package com.messenger.service;

import com.messenger.exp.BadRequestException;
import com.messenger.model.dto.message.MessageEditDTO;
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

import javax.transaction.Transactional;
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

        UserEntity user = userRepository.findByUsernameForServices(senderUsername);

        MessageListDTO messageListDTOS = new MessageListDTO();
        for (MessageEntity message : messages) {
            if (message.getSenderId() == user.getId()) {
                messageListDTOS.setOwnerMessage(List.of(message.getMessage()));
            } else {
                messageListDTOS.setFriendsMessage(List.of(message.getMessage()));
            }
        }

        return messageListDTOS;
    }

    @Transactional
    public String sendMessage(MessageSendDTO messageSendDTO, String username) {

        UserEntity owner = userRepository.findByUsernameForServices(username);

        Optional<UserEntity> friend = userRepository.findById(messageSendDTO.getFriendId());
        Optional<ChatEntity> chat = chatRepository.findById(messageSendDTO.getChatId());

        if ((friend.isEmpty() || friend.get().isDeleted()) && chat.isEmpty()) {
            throw new BadRequestException("User not found");
        }


        ChatEntity chatEntity;
        if (chat.isEmpty()) {
            ChatUserEntity chatUser = chatUserRepository.save(
                    ChatUserEntity.builder()
                            .chat(new ChatEntity())
                            .user(owner)
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
                        .senderId(owner.getId())
                        .message(messageSendDTO.getMessage())
                        .chat(chatEntity)
                        .build()
        );

        return "Sent";
    }

    @Transactional
    public String updateMessage(MessageEditDTO messageEditDTO, String currentPrincipalName) {

        UserEntity owner = userRepository.findByUsernameForServices(currentPrincipalName);

        Optional<MessageEntity> message = messageRepository.findById(messageEditDTO.getMessageId());

        if (message.isEmpty()) {
            throw new BadRequestException("Message not found");
        }

        if (messageEditDTO.getMessage().isEmpty()) {
            return "Updated";
        }

        messageRepository.save(
                MessageEntity.builder()
                        .id(message.get().getId())
                        .chat(message.get().getChat())
                        .senderId(owner.getId())
                        .message(messageEditDTO.getMessage())
                        .build()
        );

        return "Updated";
    }
}
