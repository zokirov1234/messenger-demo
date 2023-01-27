package com.messenger.repository;

import com.messenger.model.entity.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<GroupEntity, Integer> {

    @Modifying
    @Query("update GroupEntity g set g.numberOfUsers = ?1 where g.id = ?2")
    void setNumberOfUsers(int newUsers, int groupId);

    Optional<GroupEntity> findByChatId(Integer chatId);
}
