package com.messenger.repository;

import com.messenger.model.entity.ChannelUserEntity;
import com.messenger.model.enums.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChannelUserRepository extends JpaRepository<ChannelUserEntity, Integer> {

    List<ChannelUserEntity> findByChannelId(Integer channelId);

    @Query("select ch from ChannelUserEntity ch where ch.subscribers.id = ?1 and ch.channel.id = ?2")
    ChannelUserEntity findBySubscribersId(int userId, int channelId);

    @Modifying
    @Query("update ChannelUserEntity ch set ch.permission=?1 where ch.channel.id=?2 and ch.subscribers.id =?3")
    void setAsAdmin(Permission permission, int channelId, int userId);
}
