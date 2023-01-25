package com.messenger.model.dto.message;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MessageListDTO {
    private List<String> ownerMessage;
    private List<String> friendsMessage;
}
