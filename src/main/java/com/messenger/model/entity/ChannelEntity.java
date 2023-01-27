package com.messenger.model.entity;

import com.messenger.model.enums.Types;
import lombok.*;

import javax.persistence.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "channel")
public class ChannelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String description;

    private int numberOfSubscribers;

    @OneToOne(cascade = CascadeType.ALL)
    private ChatTypes chatTypes;

    @OneToOne(cascade = CascadeType.ALL)
    private ChatEntity chat;
}
