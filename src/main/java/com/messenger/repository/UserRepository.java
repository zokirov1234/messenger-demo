package com.messenger.repository;

import com.messenger.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findByUsername(String username);
    @Query("select u from UserEntity u where u.username = ?1 and u.isDeleted = false")
    UserEntity findByUsernameForServices(String username);

    Optional<UserEntity> findByPhoneNumber(String phoneNumber);

    @Query("select u from UserEntity u where u.isDeleted = false")
    List<UserEntity> findAllIsNotDeleted();

    @Query("SELECT u FROM UserEntity u where u.username like %?1% and u.isDeleted = false")
    List<UserEntity> getAllByUsername(String username);
    @Modifying
    @Query("update UserEntity u set u.isDeleted = true where u.username = ?1")
    void setDeleted(String username);
}
