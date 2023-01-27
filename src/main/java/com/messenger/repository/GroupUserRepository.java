package com.messenger.repository;

import com.messenger.model.entity.GroupUserEntity;
import com.messenger.model.enums.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface GroupUserRepository extends JpaRepository<GroupUserEntity, Integer> {
    @Modifying
    @Query("delete from GroupUserEntity g where g.group.id = ?1 and g.participants.id = ?2")
    void deleteByGroupIdAndUserId(int groupId, int userId);


    Optional<GroupUserEntity> findByGroupIdAndParticipantsId(int groupId, int userId);

    @Modifying
    @Query("update GroupUserEntity g set g.permission=?1 where g.group.id=?2 and g.participants.id =?3")
    void setAsAdmin(Permission permission, int groupId, int userId);
}
