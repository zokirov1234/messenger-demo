package com.messenger.model.dto.message;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class MessageSearchWithoutDate {

    private String message;
    private int chatId;
}
