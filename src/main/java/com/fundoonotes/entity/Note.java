package com.fundoonotes.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "notes")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    private boolean isPinned;

    private boolean isArchived;

    private boolean isTrashed;

    @Column(nullable = false)
    private Long userId;
}
