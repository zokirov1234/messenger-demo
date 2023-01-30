package com.messenger.model.dto.message;

import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MessageGetResponseDTO {

    private String message;
    private String username;
    private int messageId;
    private int chatId;
    private Timestamp sent;
}
