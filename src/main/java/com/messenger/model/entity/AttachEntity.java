package com.messenger.model.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
    private int id;

    @Column
    private String extension;
    @Column(name = "original_name")
    private String originalName;
    @Column
    private Long size;
    @Column
    private String path;
    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
}
