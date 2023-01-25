package com.messenger.service;

import com.messenger.model.dto.user.UserResponseDTO;
import com.messenger.model.entity.UserEntity;
import com.messenger.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponseDTO> getAllUser() {
        List<UserEntity> users = userRepository.findAll();
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
}
