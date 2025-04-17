package com.mobylab.springbackend.repository;

import com.mobylab.springbackend.entity.Payment;
import com.mobylab.springbackend.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}
