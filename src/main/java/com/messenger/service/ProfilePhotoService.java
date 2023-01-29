package com.messenger.service;

import com.messenger.exp.ItemNotFoundException;
import com.messenger.exp.NotPermissionException;
import com.messenger.model.dto.attach.AttachDTO;
import com.messenger.model.entity.*;
import com.messenger.model.enums.Permission;
import com.messenger.model.enums.ProfileType;
import com.messenger.repository.*;
import com.messenger.util.CurrentUserUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@AllArgsConstructor
@Service
public class ProfilePhotoService {

    private final UserRepository userRepository;
    private final ProfilePhotoRepository profilePhotoRepository;

    private final GroupRepository groupRepository;

    private final GroupUserRepository groupUserRepository;
    private final AttachService attachService;

    private final ChannelRepository channelRepository;

    private final ChannelUserRepository channelUserRepository;


    public String setPhotoOnUser(MultipartFile file) {
        UserEntity user = userRepository.findByUsernameForServices(CurrentUserUtil.getCurrentUser());

        AttachDTO attached = attachService.attached(file);

        profilePhotoRepository.save(
                ProfilePhotoEntity.builder()
                        .profileId(user.getId())
                        .photoId(attached.getId())
                        .profileType(ProfileType.USER)
                        .build()
        );

        return "success";
    }

    public String setPhotoOnGroupProfile(MultipartFile file, int groupId) {

        UserEntity isAdmin = userRepository.findByUsernameForServices(CurrentUserUtil.getCurrentUser());

        groupRepository.findById(groupId).orElseThrow(() -> {
            throw new ItemNotFoundException("Group not found");
        });

        GroupUserEntity groupUser = groupUserRepository.findByGroupIdAndParticipantsId(groupId, isAdmin.getId()).orElseThrow(() -> {
            throw new ItemNotFoundException("User not found");
        });

        if (groupUser.getPermission() != Permission.ADMIN) {
            throw new NotPermissionException("Not allowed");
        }

        AttachDTO attached = attachService.attached(file);

        profilePhotoRepository.save(ProfilePhotoEntity.builder()
                .profileType(ProfileType.GROUP)
                .photoId(attached.getId())
                .profileId(groupId)
                .build());

        return "success";
    }

    public String setPhotoOnChannelProfile(MultipartFile file, Integer channelId) {

        UserEntity user = userRepository.findByUsernameForServices(CurrentUserUtil.getCurrentUser());

        channelRepository.findById(channelId).orElseThrow(() -> {
            throw new ItemNotFoundException("Item not found");
        });

        ChannelUserEntity channelSubscriber = channelUserRepository.findBySubscribersIdAndChannelId(user.getId(), channelId);

        if (channelSubscriber == null) {
            throw new ItemNotFoundException("User not found");
        }

        AttachDTO attached = attachService.attached(file);


        profilePhotoRepository.save(ProfilePhotoEntity.builder()
                .profileId(channelId)
                .photoId(attached.getId())
                .profileType(ProfileType.CHANNEL)
                .build());

        return "success";
    }
}
