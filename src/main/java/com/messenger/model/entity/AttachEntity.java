package com.messenger.model.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "attach")
public class AttachEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String extension;
    @Column(name = "original_name")
    private String originalName;
    @Column
    private Long size;
    @Column
    private String path;
    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
}
