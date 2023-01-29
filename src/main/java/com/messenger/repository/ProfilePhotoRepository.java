package com.messenger.repository;

import com.messenger.model.entity.ProfilePhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfilePhotoRepository extends JpaRepository<ProfilePhotoEntity, Integer> {
}
