package com.messenger.model.entity;

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
@Table(name = "groups")
public class GroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String name;

    private String description;

    private int numberOfUsers;

    @OneToOne(cascade = CascadeType.ALL)
    private ChatEntity chat;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;
}
