package com.messenger.model.dto.channel;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ChannelAddUserResponseDTO {

    private int numberOfSubscribers;
    private String addingOwner;

}
