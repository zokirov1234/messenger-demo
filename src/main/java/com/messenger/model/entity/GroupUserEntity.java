package com.messenger.model.entity;

import com.messenger.model.enums.Permission;
import lombok.*;

import javax.persistence.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "group_user")
public class GroupUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn
    private UserEntity participants;

    @Enumerated(EnumType.STRING)
    private Permission permission;

    @ManyToOne
    private GroupEntity group;
}
