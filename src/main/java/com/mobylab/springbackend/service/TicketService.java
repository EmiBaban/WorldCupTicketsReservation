package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.Ticket;
import com.mobylab.springbackend.entity.User;
import com.mobylab.springbackend.repository.TicketRepository;
import com.mobylab.springbackend.repository.UserRepository;
import com.mobylab.springbackend.service.dto.TicketDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class TicketService {

    @Autowired
    private  TicketRepository ticketRepository;
    @Autowired
    private  UserRepository userRepository;

    public List<TicketDto> getAllTickets() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Ticket> tickets = ticketRepository.findByUser(user);

        return tickets.stream().map(ticket -> new TicketDto(
                ticket.getMatch().getId(),
                ticket.getPrice(),
                ticket.getPurchaseTime()
        )).toList();
    }


}
