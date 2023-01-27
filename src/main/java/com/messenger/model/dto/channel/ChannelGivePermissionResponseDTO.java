package com.messenger.model.dto.channel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ChannelGivePermissionResponseDTO {

    private String newAdmin;
    private String message;
}
