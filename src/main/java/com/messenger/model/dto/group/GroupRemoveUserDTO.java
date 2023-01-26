package com.messenger.model.dto.group;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class GroupRemoveUserDTO {
    private int chatId;
    private List<String> usernames;
}
