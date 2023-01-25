package com.messenger.model.dto.chat;

import com.messenger.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ChatOfUsers {

    private UserEntity user;
    private int chatId;
}
