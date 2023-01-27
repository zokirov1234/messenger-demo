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
@Table(name = "channel_user")
public class ChannelUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private Permission permission;

    @ManyToOne
    @JoinColumn
    private UserEntity subscribers;

    @ManyToOne(cascade = CascadeType.ALL)
    private ChannelEntity channel;
}
