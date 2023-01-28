package com.messenger.repository;

import com.messenger.model.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity, Integer> {

    @Query("select m from MessageEntity m where m.chat.id = ?1 order by m.createdAt asc")
    List<MessageEntity> getAllByChatId(int id);

    void deleteByChatId(int chatId);
}
