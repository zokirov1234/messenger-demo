package com.messenger.service;

import com.messenger.exp.BadRequestException;
import com.messenger.model.entity.ChatTypes;
import com.messenger.model.enums.Types;
import com.messenger.repository.ChatTypesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@AllArgsConstructor
@Service
public class ChatTypeService {

    private final ChatTypesRepository chatTypesRepository;

    public ChatTypes createType(String link, String type) {

        Optional<ChatTypes> types = chatTypesRepository.findByLink(link);

        if (types.isPresent()) {
            throw new BadRequestException("Link already token");
        }

        ChatTypes chatTypes = new ChatTypes();

        if (type.equals(Types.PRIVATE.name())) {
            chatTypes.setLink(generateLink());
        } else {
            chatTypes.setLink(link);
        }

        chatTypes.setTypes(Types.valueOf(type));

        return chatTypes;
    }

    private String generateLink() {
        String link = "";
        Random random = new Random();
        for (int i = 0; i < 16; i++) {
            char chars = (char) random.nextInt(65, 123);
            link = link + chars;
        }

        return link;
    }
}
