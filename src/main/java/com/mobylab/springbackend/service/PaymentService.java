package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.Payment;
import com.mobylab.springbackend.entity.Seat;
import com.mobylab.springbackend.entity.Ticket;
import com.mobylab.springbackend.entity.User;
import com.mobylab.springbackend.enums.SeatStatus;
import com.mobylab.springbackend.exception.BadRequestException;
import com.mobylab.springbackend.exception.NotFoundException;
import com.mobylab.springbackend.repository.*;
import com.mobylab.springbackend.service.dto.PaymentRequestDto;
import com.mobylab.springbackend.service.dto.PaymentResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final SeatRepository seatRepository;
    private final TicketRepository ticketRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public PaymentResponseDto checkout(PaymentRequestDto request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        List<Seat> seats = seatRepository.findAllByStatus(SeatStatus.SELECTAT);

        if (seats.isEmpty()) {
            throw new BadRequestException("Nu exista locuri selectate.");
        }

        String paymentMethod = request.getPaymentMethod();
        if (paymentMethod == null || paymentMethod.isBlank()) {
            throw new BadRequestException("Metoda de plata este obligatorie.");
        }

        List<String> validMethods = List.of("card", "paypal");
        if (!validMethods.contains(paymentMethod.toLowerCase())) {
            throw new BadRequestException("Metoda de plata invalida.");
        }

        Payment payment = new Payment();
        payment.setUser(user);
        payment.setPaymentMethod(paymentMethod);
        payment.setAmount(0.0);
        payment.setTimestamp(LocalDateTime.now());
        payment.setIsPaid(true);
        payment = paymentRepository.save(payment);

        double totalPrice = 0.0;
        List<Ticket> tickets = new ArrayList<>();

        for (Seat seat : seats) {
            if (!seat.getStatus().equals(SeatStatus.SELECTAT)) {
                throw new RuntimeException("Locul nu este disponibil");
            }

            seat.setStatus(SeatStatus.OCUPAT);
            Seat savedSeat = seatRepository.save(seat);

            Ticket ticket = new Ticket();
            ticket.setMatch(seat.getMatch());
            ticket.setSeat(savedSeat);
            ticket.setUser(user);
            ticket.setPrice(seat.getMatch().getSeatPrice());
            ticket.setPurchaseTime(LocalDateTime.now());
            ticket.setPayment(payment); // setăm payment-ul

            ticket = ticketRepository.save(ticket);
            tickets.add(ticket);

            totalPrice += ticket.getPrice();
        }

        payment.setAmount(totalPrice);
        paymentRepository.save(payment);


        emailService.sendConfirmation(user.getEmail(), tickets);

        return new PaymentResponseDto(payment.getId(), totalPrice, tickets.size());
    }
}
