package com.messenger.model.dto.channel;

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
public class ChannelCreatedRequest {

    @NotBlank
    private String channelName;
    private String description;
    @NotNull
    private String channelType;
    private String chatLink;

}
