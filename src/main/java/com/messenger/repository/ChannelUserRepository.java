package com.messenger.repository;

import com.messenger.model.entity.ChannelUserEntity;
import com.messenger.model.enums.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface ChannelUserRepository extends JpaRepository<ChannelUserEntity, Integer> {

    ChannelUserEntity findBySubscribersIdAndChannelId(int userId, int channelId);

    @Modifying
    @Query("update ChannelUserEntity ch set ch.permission=?1 where ch.channel.id=?2 and ch.subscribers.id =?3")
    void setPermission(Permission permission, int channelId, int userId);

    @Modifying
    void deleteByChannelIdAndSubscribersId(int channelId, int subscriberId);


    void deleteByChannelId(int channelId);
}
