package com.messenger.repository;

import com.messenger.model.entity.ChannelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChannelRepository extends JpaRepository<ChannelEntity, Integer> {

    Optional<ChannelEntity> findByName(String name);

    @Modifying
    @Query("update ChannelEntity ch set ch.numberOfSubscribers = ?1 where ch.id = ?2")
    void setNumberOfSubscribers(int amount, int channelId);
}
