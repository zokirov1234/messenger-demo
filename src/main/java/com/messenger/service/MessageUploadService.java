package com.messenger.service;

import com.messenger.exp.ItemNotFoundException;
import com.messenger.model.dto.attach.AttachDTO;
import com.messenger.model.entity.ChatEntity;
import com.messenger.model.entity.MessageEntity;
import com.messenger.model.entity.UserEntity;
import com.messenger.model.enums.MessageTypes;
import com.messenger.repository.ChatRepository;
import com.messenger.repository.MessageRepository;
import com.messenger.repository.UserRepository;
import com.messenger.util.CurrentUserUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

@AllArgsConstructor
@Service
@Transactional
public class MessageUploadService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    private final ChatRepository chatRepository;

    private final AttachService attachService;


    public String uploadMessage(MultipartFile file, int chatId) {

        UserEntity user = userRepository.findByUsernameForServices(CurrentUserUtil.getCurrentUser());

        ChatEntity chat = chatRepository.findById(chatId).orElseThrow(() -> {
            throw new ItemNotFoundException("Chat not found");
        });

        AttachDTO attachDTO = attachService.attached(file);

        MessageTypes messageTypes = switch (attachDTO.getExtension()) {
            case "mp4" -> MessageTypes.VIDEO;
            case "mp3" -> MessageTypes.AUDIO;
            case "pdf", "doc", "txt", "odt", "odf" -> MessageTypes.DOCUMENT;
            case "png", "jpg" -> MessageTypes.PHOTO;
            default -> null;
        };

        messageRepository.save(
                MessageEntity.builder()
                        .type(messageTypes)
                        .message(String.valueOf(attachDTO.getId()))
                        .senderId(user.getId())
                        .chat(chat)
                        .build()
        );

        return "success";
    }

}
