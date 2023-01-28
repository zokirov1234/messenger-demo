package com.messenger.model.dto.channel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ChannelGetPermissionReceiveDTO {
    @NotBlank
    private String username;
    @NotEmpty
    private int channelId;
}
