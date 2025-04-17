package com.mobylab.springbackend.controller;

import com.mobylab.springbackend.service.TicketService;
import com.mobylab.springbackend.service.dto.TicketDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController implements SecuredRestController {

    private final TicketService ticketService;

    @GetMapping("/my")
    public ResponseEntity<List<TicketDto>> getMyTickets() {
        List<TicketDto> tickets = ticketService.getAllTickets();
        return ResponseEntity.ok(tickets);
    }
}
