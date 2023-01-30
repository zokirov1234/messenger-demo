package com.messenger.model.dto.chat;

import com.messenger.model.dto.message.MessageGetResponseDTO;
import com.messenger.model.entity.ChatTypes;
import com.messenger.model.enums.ProfileType;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ChatResponseListDTO {
    private String chatName;
    private ProfileType chatType;
    private List<MessageGetResponseDTO> messages;
}
