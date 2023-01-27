package com.messenger.model.dto.channel;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ChannelCreatedResponse {

    private int channelId;
    private String name;
    private int numberOfSubscribers;
    private String description;
    private String link;
    private List<String> subscribers;
}
