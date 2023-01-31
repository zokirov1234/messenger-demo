package com.messenger.service;

import com.messenger.exp.BadRequestException;
import com.messenger.model.dto.chat.ChatResponseListDTO;
import com.messenger.model.dto.message.MessageGetResponseDTO;
import com.messenger.model.dto.user.UserResponseDTO;
import com.messenger.model.dto.user.UserUpdateProfile;
import com.messenger.model.entity.*;
import com.messenger.model.enums.ProfileType;
import com.messenger.repository.*;
import com.messenger.util.CurrentUserUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final MessageRepository messageRepository;

    private final ChatUserRepository chatUserRepository;

    private final GroupRepository groupRepository;

    private final ChannelRepository channelRepository;

    private final PasswordEncoder passwordEncoder;

    public List<UserResponseDTO> getAllUser() {
        List<UserEntity> users = userRepository.findAllIsNotDeleted();
        List<UserResponseDTO> userResponseDTOList = new ArrayList<>();
        for (UserEntity user : users) {
            userResponseDTOList.add(
                    new UserResponseDTO(
                            user.getId(),
                            user.getUsername(),
                            user.getPhoneNumber(),
                            user.getName()
                    )
            );
        }

        return userResponseDTOList;
    }

    public List<UserResponseDTO> findByUsername(String username) {

        List<UserEntity> users = userRepository.getAllByUsername(username);

        List<UserResponseDTO> userResponseDTOList = new ArrayList<>();
        for (UserEntity user : users) {
            userResponseDTOList.add(
                    new UserResponseDTO(
                            user.getId(),
                            user.getUsername(),
                            user.getPhoneNumber(),
                            user.getName()
                    )
            );
        }

        return userResponseDTOList;
    }

    public String editProfile(UserUpdateProfile userUpdateProfile, String username) {

        UserEntity user = userRepository.findByUsernameForServices(username);

        Optional<UserEntity> newUserName = userRepository.findByUsername(userUpdateProfile.getUsername());

        if (newUserName.isPresent()) {
            throw new BadRequestException("Username already exists");
        }

        if (userRepository.findByPhoneNumber(userUpdateProfile.getPhoneNumber()).isPresent()) {
            throw new BadRequestException("This phone number already exists");
        }

        userRepository.save(
                UserEntity.builder()
                        .id(user.getId())
                        .name(userUpdateProfile.getName())
                        .username(userUpdateProfile.getUsername())
                        .phoneNumber(userUpdateProfile.getPhoneNumber())
                        .password(passwordEncoder.encode(userUpdateProfile.getPassword()))
                        .build()
        );

        return "success";
    }

    @Transactional
    public String deleteProfile(String username) {

        userRepository.setDeleted(username);

        return "success";
    }

    public List<ChatResponseListDTO> getAllChats() {

        UserEntity user = userRepository.findByUsernameForServices(CurrentUserUtil.getCurrentUser());

        List<ChatResponseListDTO> responseListDTO = new ArrayList<>();

        for (MessageEntity message : messageRepository.findBySenderId(user.getId())) {

            Optional<ChannelEntity> channel = channelRepository.findByChatId(message.getChat().getId());
            Optional<GroupEntity> group = groupRepository.findByChatId(message.getChat().getId());

            if (channel.isPresent()) {
                List<MessageGetResponseDTO> list = new ArrayList<>();
                list.add(MessageGetResponseDTO.builder()
                        .chatId(message.getChat().getId())
                        .messageId(message.getId())
                        .sent(message.getCreatedAt())
                        .message(message.getMessage())
                        .username(userRepository.findById(message.getSenderId()).get().getUsername())
                        .build());
                responseListDTO.add(ChatResponseListDTO.builder()
                        .chatName(channel.get().getName())
                        .chatType(ProfileType.CHANNEL)
                        .messages(list)
                        .build());
            } else if (group.isPresent()) {
                List<MessageGetResponseDTO> responseDTOS = new ArrayList<>();
                responseDTOS.add(MessageGetResponseDTO.builder()
                        .chatId(message.getChat().getId())
                        .messageId(message.getId())
                        .sent(message.getCreatedAt())
                        .message(message.getMessage())
                        .username(userRepository.findById(message.getSenderId()).get().getUsername())
                        .build());
                responseListDTO.add(ChatResponseListDTO.builder()
                        .chatName(group.get().getName())
                        .chatType(ProfileType.GROUP)
                        .messages(responseDTOS)
                        .build());
            } else {
                List<MessageGetResponseDTO> responseDTOS = new ArrayList<>();
                Optional<ChatUserEntity> friend = chatUserRepository.getFriend(message.getChat().getId(), message.getSenderId());
                responseDTOS.add(MessageGetResponseDTO.builder()
                        .chatId(message.getChat().getId())
                        .messageId(message.getId())
                        .sent(message.getCreatedAt())
                        .message(message.getMessage())
                        .username(userRepository.findById(message.getSenderId()).get().getUsername())
                        .build());
                responseListDTO.add(ChatResponseListDTO.builder()
                        .chatName(friend.get().getUser().getName())
                        .chatType(ProfileType.USER)
                        .messages(responseDTOS)
                        .build());
            }
        }

        return responseListDTO;
    }

    public List<ChatResponseListDTO> getAllPrivateChats() {
        UserEntity user = userRepository.findByUsernameForServices(CurrentUserUtil.getCurrentUser());

        List<ChatResponseListDTO> responseListDTOS = new ArrayList<>();
        for (ChatUserEntity chatUser : chatUserRepository.findByUserId(user.getId(), ProfileType.USER)){
            List<MessageEntity> messages = messageRepository.getAllByChatId(chatUser.getChat().getId());

            for (MessageEntity messageEntity : messages){
                List<MessageGetResponseDTO> getResponseDTOS = new ArrayList<>();
                Optional<ChatUserEntity> friend = chatUserRepository.getFriend(messageEntity.getChat().getId(), messageEntity.getSenderId());
                getResponseDTOS.add(MessageGetResponseDTO.builder()
                        .chatId(messageEntity.getChat().getId())
                        .messageId(messageEntity.getId())
                        .sent(messageEntity.getCreatedAt())
                        .message(messageEntity.getMessage())
                        .username(userRepository.findById(messageEntity.getSenderId()).get().getUsername())
                        .build());
                responseListDTOS.add(ChatResponseListDTO.builder()
                                .messages(getResponseDTOS)
                                .chatName(friend.get().getUser().getName())
                                .chatType(ProfileType.USER)
                        .build());
            }
        }

        return responseListDTOS;
    }
}
