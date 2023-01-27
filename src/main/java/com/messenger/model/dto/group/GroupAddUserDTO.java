package com.messenger.model.dto.group;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class GroupAddUserDTO {

    @NotEmpty
    private List<String> usernames;
    @NotEmpty
    private int chatId;
    @NotEmpty
    private int groupId;
}
