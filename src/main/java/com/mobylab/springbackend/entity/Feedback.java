package com.mobylab.springbackend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private String category;
    private String message;
    private int rating;
    private boolean recommend;

    @ManyToOne
    private User user;

    private LocalDateTime submittedAt;
}