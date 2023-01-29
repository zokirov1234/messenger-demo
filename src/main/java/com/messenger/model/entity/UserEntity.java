package com.messenger.model.entity;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;

    private String bio;

    private boolean isDeleted;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<RoleEntity> userRoles = new HashSet<>();

    @CreationTimestamp
    @Column(name = "registered_at")
    private Timestamp createdAt;
}
