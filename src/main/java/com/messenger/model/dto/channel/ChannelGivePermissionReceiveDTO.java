package com.messenger.model.dto.channel;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ChannelGivePermissionReceiveDTO {

    @NotBlank
    private String username;
    @NotNull
    private int channelId;
}
