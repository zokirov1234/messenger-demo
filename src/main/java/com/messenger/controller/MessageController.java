package com.messenger.controller;

import com.messenger.model.dto.message.MessageListDTO;
import com.messenger.model.dto.message.MessageSendDTO;
import com.messenger.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
@AllArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/{chatId}")
    public ResponseEntity<MessageListDTO> getAllMessages(
            @PathVariable("chatId") int chatId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        MessageListDTO allMessages = messageService.getAllMessages(chatId, currentPrincipalName);
        return ResponseEntity.ok().body(allMessages);
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(
            @RequestBody MessageSendDTO messageSendDTO
            ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        String response = messageService.sendMessage(messageSendDTO, currentPrincipalName);

        return ResponseEntity.ok().body(response);
    }

}
