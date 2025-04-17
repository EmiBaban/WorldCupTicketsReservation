package com.mobylab.springbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue()
    private UUID id;

    private LocalDateTime timestamp;

    private Double amount;

    private String paymentMethod;

    private Boolean isPaid;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
    private List<Ticket> tickets;
}
