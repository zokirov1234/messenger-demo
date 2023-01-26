package com.messenger.repository;

import com.messenger.model.entity.ChatUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatUserRepository extends JpaRepository<ChatUserEntity, Integer> {

    @Query(value = "select ch from ChatUserEntity ch " +
            "where ch.chat.id in (select cha.chat.id from ChatUserEntity cha where cha.user.id = ?1) and " +
            "ch.user.id <> ?1")
    List<ChatUserEntity> getFriendsByChatId(int userId);

    @Modifying
    Integer deleteByUserIdAndChatId(int userId, int chatId);
}
