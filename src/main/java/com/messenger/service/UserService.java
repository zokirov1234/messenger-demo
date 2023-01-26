package com.messenger.service;

import com.messenger.exp.BadRequestException;
import com.messenger.model.dto.user.UserResponseDTO;
import com.messenger.model.dto.user.UserUpdateProfile;
import com.messenger.model.entity.UserEntity;
import com.messenger.repository.UserRepository;
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

        return "Successfully edited";
    }

    @Transactional
    public String deleteProfile(String username) {

        userRepository.setDeleted(username);

        return "Deleted successfully";
    }
}
