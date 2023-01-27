package com.messenger.model.dto.channel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ChannelBroadCastReceiveDTO {

    @NotNull
    private int channelId;
    private String message;
}
