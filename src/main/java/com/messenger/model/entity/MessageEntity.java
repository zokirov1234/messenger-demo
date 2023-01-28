package com.messenger.model.entity;

import com.messenger.model.enums.MessageTypes;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "message")
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String message;

    private int senderId;

    @Enumerated(EnumType.STRING)
    private MessageTypes type;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private ChatEntity chat;

    @CreationTimestamp
    @Column(name = "sent_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "edited_at")
    private Timestamp modifiedAt;
}
