package com.messenger.controller;


import com.messenger.model.dto.chat.ChatUserDTO;
import com.messenger.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@AllArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/getAll")
    public ResponseEntity<List<ChatUserDTO>> getChats() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        List<ChatUserDTO> chatOfUser = chatService.getChatOfUser(currentPrincipalName);

        return ResponseEntity.ok().body(chatOfUser);
    }


}
