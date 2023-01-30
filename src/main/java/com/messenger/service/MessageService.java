package com.messenger.service;

import com.messenger.exp.ItemNotFoundException;
import com.messenger.model.dto.message.*;
import com.messenger.model.entity.ChatEntity;
import com.messenger.model.entity.ChatUserEntity;
import com.messenger.model.entity.MessageEntity;
import com.messenger.model.entity.UserEntity;
import com.messenger.model.enums.MessageTypes;
import com.messenger.model.enums.PinType;
import com.messenger.repository.ChatRepository;
import com.messenger.repository.ChatUserRepository;
import com.messenger.repository.MessageRepository;
import com.messenger.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
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


    public String sendMessage(MessageSendDTO messageSendDTO, String username) {

        UserEntity owner = userRepository.findByUsernameForServices(username);

        Optional<UserEntity> friend = userRepository.findById(messageSendDTO.getFriendId());
        Optional<ChatEntity> chat = chatRepository.findById(messageSendDTO.getChatId());

        if ((friend.isEmpty() || friend.get().isDeleted()) && chat.isEmpty()) {
            throw new ItemNotFoundException("User not found");
        }


        ChatEntity save = chatRepository.save(new ChatEntity());
        if (chat.isEmpty()) {
            chatUserRepository.save(
                    ChatUserEntity.builder()
                            .chat(save)
                            .user(owner)
                            .build()
            );

            chatUserRepository.save(
                    ChatUserEntity.builder()
                            .chat(save)
                            .user(friend.get())
                            .build()
            );
        } else {
            save = chat.get();
        }

        messageRepository.save(
                MessageEntity.builder()
                        .senderId(owner.getId())
                        .message(messageSendDTO.getMessage())
                        .pinType(PinType.UNPINNED)
                        .type(MessageTypes.TEXT)
                        .chat(save)
                        .build()
        );

        return "success";
    }


    public String updateMessage(MessageEditDTO messageEditDTO, String currentPrincipalName) {

        UserEntity owner = userRepository.findByUsernameForServices(currentPrincipalName);

        MessageEntity message = messageRepository.findById(messageEditDTO.getMessageId()).orElseThrow(() -> {
            throw new ItemNotFoundException("Message not found");
        });

        if (messageEditDTO.getMessage().isEmpty()) {
            return "success";
        }

        messageRepository.updateMessage(messageEditDTO.getMessage(), message.getId());

        return "success";
    }

    public String pinMessage(MessagePinDTO pinDTO) {

        messageRepository.findById(pinDTO.getMessageId()).orElseThrow(() -> {
            throw new ItemNotFoundException("Message not found");
        });

        chatRepository.findById(pinDTO.getChatId()).orElseThrow(() -> {
            throw new ItemNotFoundException("Chat not found");
        });

        messageRepository.setAsPinned(PinType.PINNED, pinDTO.getMessageId(), pinDTO.getChatId());

        return "success";
    }

    public List<MessageSearchResponseDTO> searchWithoutDate(MessageSearchWithoutDate dto) {

        List<MessageSearchResponseDTO> dtoList = new ArrayList<>();
        messageRepository.findByMessageAndChatIdWithoutDate(dto.getMessage(), dto.getChatId())
                .forEach(messageEntity -> dtoList.add(
                        MessageSearchResponseDTO.builder()
                                .createdAt(messageEntity.getCreatedAt())
                                .message(messageEntity.getMessage())
                                .build()
                ));

        return dtoList;
    }

    public List<MessageSearchResponseDTO> searchWithDate(MessageSearchWithDate dto) throws ParseException {

        List<MessageSearchResponseDTO> dtoList = new ArrayList<>();

        messageRepository.findByMessageAndChatIdWithDate(dto.getMessage(), dto.getChatId(), dto.getDate())
                .forEach(messageEntity -> dtoList.add(
                        MessageSearchResponseDTO.builder()
                                .createdAt(messageEntity.getCreatedAt())
                                .message(messageEntity.getMessage())
                                .build()
                ));

        return dtoList;
    }
}
