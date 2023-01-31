package com.messenger.model.entity;

import com.messenger.model.enums.ProfileType;
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
@Table(name = "chat")
public class ChatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private ProfileType profileType;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

}
