package com.messenger.controller;

import com.messenger.model.dto.message.MessageEditDTO;
import com.messenger.model.dto.message.MessageListDTO;
import com.messenger.model.dto.message.MessageSendDTO;
import com.messenger.service.MessageService;
import com.messenger.util.CurrentUserUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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
        MessageListDTO allMessages = messageService.getAllMessages(chatId, CurrentUserUtil.getCurrentUser());
        return ResponseEntity.ok().body(allMessages);
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(
            @RequestBody MessageSendDTO messageSendDTO
    ) {
        String response = messageService.sendMessage(messageSendDTO, CurrentUserUtil.getCurrentUser());

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/edit")
    public ResponseEntity<?> editMessage(
            @RequestBody MessageEditDTO messageEditDTO
    ) {
        String response = messageService.updateMessage(messageEditDTO, CurrentUserUtil.getCurrentUser());

        return ResponseEntity.ok(response);
    }

}
