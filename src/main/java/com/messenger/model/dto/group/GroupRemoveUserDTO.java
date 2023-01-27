package com.messenger.model.dto.group;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class GroupRemoveUserDTO {
    @NotNull
    private int chatId;
    private List<String> usernames;
}
