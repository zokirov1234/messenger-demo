package com.messenger.model.dto.channel;

import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ChannelBroadCastResponseDTO {

    private String message;
    private Timestamp createdAt;
}
