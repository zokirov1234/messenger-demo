package com.messenger.repository;

import com.messenger.model.entity.ChatUserEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface ChatUserRepository extends JpaRepository<ChatUserEntity, Integer> {

    @Query(value = "select ch from ChatUserEntity ch " +
            "where ch.chat.id in (select cha.chat.id from ChatUserEntity cha where cha.user.id = ?1) and " +
            "ch.user.id <> ?1")
    List<ChatUserEntity> getFriendsByChatId(int userId);

    @Query("select ch from ChatUserEntity ch where ch.chat.id = ?1 and ch.user.id <> ?2")
    Optional<ChatUserEntity> getFriend(int chatId, int userId);


    Optional<ChatUserEntity> findByChatIdAndUserId(int chatId, int userId);

    @Modifying
    void deleteByUserIdAndChatId(int userId, int chatId);

    @Transactional
    @Modifying
    void deleteByChatId(int chatId);
}
