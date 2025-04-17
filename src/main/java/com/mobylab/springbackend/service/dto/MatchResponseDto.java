package com.mobylab.springbackend.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class MatchResponseDto {
    private UUID id;
    private String stadiumName;
    private String homeTeamName;
    private String awayTeamName;
    private String referee;
    private double seatPrice;
    private LocalDateTime dateTime;
}

