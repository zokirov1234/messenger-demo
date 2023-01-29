package com.messenger.model.dto.message;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class MessagePinDTO {

    private int messageId;
    private int chatId;
}
