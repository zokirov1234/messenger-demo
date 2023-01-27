package com.messenger.model.entity;

import com.messenger.model.enums.Types;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "chat_types")
public class ChatTypes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String link;

    @Enumerated(EnumType.STRING)
    private Types types;

    @CreationTimestamp
    private Timestamp createdAt;
}
