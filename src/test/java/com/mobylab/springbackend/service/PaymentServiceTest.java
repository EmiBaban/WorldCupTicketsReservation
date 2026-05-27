package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.Match;
import com.mobylab.springbackend.entity.Payment;
import com.mobylab.springbackend.entity.Seat;
import com.mobylab.springbackend.entity.Ticket;
import com.mobylab.springbackend.entity.User;
import com.mobylab.springbackend.enums.SeatStatus;
import com.mobylab.springbackend.exception.BadRequestException;
import com.mobylab.springbackend.exception.NotFoundException;
import com.mobylab.springbackend.repository.PaymentRepository;
import com.mobylab.springbackend.repository.SeatRepository;
import com.mobylab.springbackend.repository.TicketRepository;
import com.mobylab.springbackend.repository.UserRepository;
import com.mobylab.springbackend.service.dto.PaymentRequestDto;
import com.mobylab.springbackend.service.dto.PaymentResponseDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private SeatRepository seatRepository;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private PaymentService paymentService;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void checkoutShouldThrowNotFoundWhenAuthenticatedUserDoesNotExist() {
        authenticateAs("missing@example.com");
        PaymentRequestDto request = request("card");

        when(userRepository.findUserByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.checkout(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User not found");

        verify(paymentRepository, never()).save(any());
    }

    @Test
    void checkoutShouldThrowBadRequestWhenNoSeatsSelected() {
        authenticateAs("user@example.com");
        User user = new User().setEmail("user@example.com");

        when(userRepository.findUserByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(seatRepository.findAllByStatus(SeatStatus.SELECTAT)).thenReturn(List.of());

        assertThatThrownBy(() -> paymentService.checkout(request("card")))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Nu exista locuri selectate.");

        verify(paymentRepository, never()).save(any());
    }

    @Test
    void checkoutShouldThrowBadRequestWhenPaymentMethodIsBlank() {
        authenticateAs("user@example.com");
        User user = new User().setEmail("user@example.com");

        when(userRepository.findUserByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(seatRepository.findAllByStatus(SeatStatus.SELECTAT)).thenReturn(List.of(selectedSeat(50.0)));

        assertThatThrownBy(() -> paymentService.checkout(request(" ")))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Metoda de plata este obligatorie.");

        verify(paymentRepository, never()).save(any());
    }

    @Test
    void checkoutShouldThrowBadRequestWhenPaymentMethodIsInvalid() {
        authenticateAs("user@example.com");
        User user = new User().setEmail("user@example.com");

        when(userRepository.findUserByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(seatRepository.findAllByStatus(SeatStatus.SELECTAT)).thenReturn(List.of(selectedSeat(50.0)));

        assertThatThrownBy(() -> paymentService.checkout(request("cash")))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Metoda de plata invalida.");

        verify(paymentRepository, never()).save(any());
    }

    @Test
    void checkoutShouldCreatePaymentTicketsAndOccupySeatsWhenRequestIsValid() {
        authenticateAs("user@example.com");
        User user = new User().setEmail("user@example.com");
        Seat firstSeat = selectedSeat(50.0);
        Seat secondSeat = selectedSeat(75.0);

        when(userRepository.findUserByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(seatRepository.findAllByStatus(SeatStatus.SELECTAT)).thenReturn(List.of(firstSeat, secondSeat));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> {
            Payment payment = invocation.getArgument(0);
            if (payment.getId() == null) {
                payment.setId(UUID.randomUUID());
            }
            return payment;
        });
        when(seatRepository.save(any(Seat.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PaymentResponseDto response = paymentService.checkout(request("card"));

        assertThat(response.getTotalAmount()).isEqualTo(125.0);
        assertThat(response.getTicketCount()).isEqualTo(2);
        assertThat(response.getPaymentId()).isNotNull();
        assertThat(firstSeat.getStatus()).isEqualTo(SeatStatus.OCUPAT);
        assertThat(secondSeat.getStatus()).isEqualTo(SeatStatus.OCUPAT);

        ArgumentCaptor<Ticket> ticketCaptor = ArgumentCaptor.forClass(Ticket.class);
        verify(ticketRepository, org.mockito.Mockito.times(2)).save(ticketCaptor.capture());
        assertThat(ticketCaptor.getAllValues())
                .extracting(Ticket::getPrice)
                .containsExactly(50.0, 75.0);

        verify(emailService).sendConfirmation(org.mockito.Mockito.eq("user@example.com"), org.mockito.Mockito.anyList());
    }

    private static void authenticateAs(String email) {
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(email, null));
    }

    private static PaymentRequestDto request(String paymentMethod) {
        PaymentRequestDto request = new PaymentRequestDto();
        request.setPaymentMethod(paymentMethod);
        return request;
    }

    private static Seat selectedSeat(double price) {
        Match match = new Match();
        match.setSeatPrice(price);

        Seat seat = new Seat();
        seat.setStatus(SeatStatus.SELECTAT);
        seat.setMatch(match);
        return seat;
    }
}
