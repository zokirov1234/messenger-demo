package com.messenger.repository;

import com.messenger.model.entity.ChatTypes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatTypesRepository extends JpaRepository<ChatTypes, Integer> {

    Optional<ChatTypes> findByLink(String link);
}
