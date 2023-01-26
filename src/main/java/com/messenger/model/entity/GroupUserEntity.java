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
    private UserEntity participants;

    @Enumerated(EnumType.STRING)
    private Permission permission;

    @ManyToOne(cascade = CascadeType.ALL)
    private GroupEntity group;
}
