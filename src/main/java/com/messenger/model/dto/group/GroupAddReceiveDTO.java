package com.messenger.model.dto.group;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class GroupAddReceiveDTO {

    @NotBlank
    private String name;
    private String description;
    @NotEmpty
    private List<String> usernames;
}
