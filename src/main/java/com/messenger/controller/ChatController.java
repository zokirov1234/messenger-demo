package com.messenger.controller;


import com.messenger.model.dto.chat.ChatUserDTO;
import com.messenger.service.ChatService;
import com.messenger.util.CurrentUserUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@AllArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/getAll")
    public ResponseEntity<List<ChatUserDTO>> getChats() {
        List<ChatUserDTO> chatOfUser = chatService.getChatOfUser(CurrentUserUtil.getCurrentUser());

        return ResponseEntity.ok().body(chatOfUser);
    }


}
