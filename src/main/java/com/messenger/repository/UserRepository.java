package com.messenger.repository;

import com.messenger.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findByUsername(String username);

    @Query("SELECT u FROM UserEntity u where u.username like %?1%")
    List<UserEntity> getAllByUsername(String username);
}
