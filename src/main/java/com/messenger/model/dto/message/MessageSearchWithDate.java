package com.messenger.model.dto.message;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class MessageSearchWithDate {

    private int chatId;
    private String message;
    private String date; // 2023/02/23
}
