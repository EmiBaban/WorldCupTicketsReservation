package com.mobylab.springbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue()
    private UUID id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Match match;

    @OneToOne
    private Seat seat;

    private Double price;

    private LocalDateTime purchaseTime;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;
}
