package com.messenger.controller;

import com.messenger.model.dto.message.*;
import com.messenger.service.MessageService;
import com.messenger.service.MessageUploadService;
import com.messenger.util.CurrentUserUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/message")
@AllArgsConstructor
public class MessageController {

    private final MessageService messageService;

    private final MessageUploadService messageUploadService;

    @GetMapping("/{chatId}")
    public ResponseEntity<MessageListDTO> getAllMessages(
            @PathVariable("chatId") int chatId
    ) {
        MessageListDTO allMessages = messageService.getAllMessages(chatId, CurrentUserUtil.getCurrentUser());
        return ResponseEntity.ok().body(allMessages);
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(
            @RequestBody @Valid MessageSendDTO messageSendDTO
    ) {
        String response = messageService.sendMessage(messageSendDTO, CurrentUserUtil.getCurrentUser());

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/edit")
    public ResponseEntity<?> editMessage(
            @RequestBody @Valid MessageEditDTO messageEditDTO
    ) {
        String response = messageService.updateMessage(messageEditDTO, CurrentUserUtil.getCurrentUser());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/upload/{chatId}")
    public ResponseEntity<?> uploadMessage(
            @PathVariable("chatId") int chatId,
            @RequestParam("file") MultipartFile multipartFile
    ) {

        String response =
                messageUploadService.uploadMessage(multipartFile, chatId);

        return ResponseEntity.ok().body(response);

    }

    @PostMapping("/pin")
    public ResponseEntity<?> pinMessage(
            @RequestBody MessagePinDTO messagePinDTO
    ) {

        String response
                = messageService.pinMessage(messagePinDTO);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/search")
    public ResponseEntity<List<MessageSearchResponseDTO>> searchMessageWithoutDate(
            @RequestBody MessageSearchWithoutDate messageSearchWithoutDate
    ) {

        List<MessageSearchResponseDTO> response
                = messageService.searchWithoutDate(messageSearchWithoutDate);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/search/date")
    public ResponseEntity<List<MessageSearchResponseDTO>> searchMessageWithDate(
            @RequestBody MessageSearchWithDate messageSearchWithDate
    ) throws ParseException {

        List<MessageSearchResponseDTO> response
                = messageService.searchWithDate(messageSearchWithDate);

        return ResponseEntity.ok().body(response);
    }

}
