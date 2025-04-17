package com.mobylab.springbackend.service.dto;

import com.mobylab.springbackend.entity.Match;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDto {
    private UUID matchId;
    private double price;
    private LocalDateTime purchaseTime;
}
