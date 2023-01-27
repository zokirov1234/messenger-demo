package com.messenger.model.dto.channel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ChannelAddUserReceiveDTO {

    @NotNull
    private int channelId;
    @NotEmpty
    private List<String> usernames;
}
