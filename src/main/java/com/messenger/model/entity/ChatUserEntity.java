package com.messenger.model.entity;

import lombok.*;

import javax.persistence.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "chat_user")
public class ChatUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private UserEntity user;

    @ManyToOne(cascade = CascadeType.ALL)
    private ChatEntity chat;
}
