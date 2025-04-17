package com.mobylab.springbackend.entity;

import com.mobylab.springbackend.enums.SeatStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "seats")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seat {

    @Id
    @GeneratedValue()
    private UUID id;

    private String row;
    private int number;

    @Enumerated(EnumType.STRING)
    private SeatStatus status; // LIBER / OCUPAT / SELECTAT
    @Column(name = "selected_at")
    private LocalDateTime selectedAt;

    @ManyToOne
    private Match match;
}

