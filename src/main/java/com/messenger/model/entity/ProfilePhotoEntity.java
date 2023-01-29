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
@Table(name = "profile_photos")
public class ProfilePhotoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "profile_id")
    private Integer profileId;

    @Column(name = "photo_id")
    private Integer photoId;

    @Enumerated(EnumType.STRING)
    private ProfileType profileType;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;
}
