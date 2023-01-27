package com.messenger.model.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class MessageSendDTO {

    @NotNull
    private int chatId;
    @NotBlank
    private String message;
    @NotNull
    private int friendId;
}
