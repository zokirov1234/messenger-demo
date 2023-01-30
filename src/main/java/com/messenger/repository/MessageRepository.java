package com.messenger.repository;

import com.messenger.model.entity.MessageEntity;
import com.messenger.model.enums.PinType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity, Integer> {

    @Query("select m from MessageEntity m where m.chat.id = ?1 order by m.createdAt asc")
    List<MessageEntity> getAllByChatId(int id);

    @Transactional
    @Modifying
    @Query("update MessageEntity m set m.pinType = ?1 where m.id = ?2 and m.chat.id = ?3")
    void setAsPinned(PinType pinType, int messageId, int chatId);

    @Modifying
    @Transactional
    @Query("update MessageEntity m set m.message =?1 where m.id =?2")
    void updateMessage(String message, int messageId);

    @Query("select m from MessageEntity m where m.message like %?1% and m.chat.id = ?2")
    List<MessageEntity> findByMessageAndChatIdWithoutDate(String message, int chatId);

    List<MessageEntity> findBySenderId(int senderId);

    @Query(value = "select m from MessageEntity m  where m.message like %?1% and m.chat.id = ?2 and m.createdAt < to_timestamp(?3, 'YYYY-MM-DD')")
    List<MessageEntity> findByMessageAndChatIdWithDate(String message, int chatId, String date);

    void deleteByChatId(int chatId);
}
