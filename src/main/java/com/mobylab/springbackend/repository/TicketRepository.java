package com.mobylab.springbackend.repository;

import com.mobylab.springbackend.entity.Ticket;
import com.mobylab.springbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    List<Ticket> findByUser(User user);
}
