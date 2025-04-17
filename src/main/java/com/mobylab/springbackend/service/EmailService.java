
package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendConfirmation(String to, List<Ticket> tickets) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Bilete rezervate");

        StringBuilder content = new StringBuilder("Ai rezervat următoarele locuri:\n");
        for (Ticket ticket : tickets) {
            content.append("• Match: ")
                    .append(ticket.getMatch().getHomeTeam().getName())
                    .append(" vs ")
                    .append(ticket.getMatch().getAwayTeam().getName())
                    .append(", Loc: ")
                    .append(ticket.getSeat().getRow()).append(ticket.getSeat().getNumber())
                    .append("\n");
        }

        message.setText(content.toString());
        mailSender.send(message);
    }
}
