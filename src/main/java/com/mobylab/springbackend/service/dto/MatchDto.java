package com.mobylab.springbackend.service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchDto {
    private UUID stadiumId;
    private UUID homeTeamId;
    private UUID awayTeamId;
    private String referee;
    private double seatPrice;
    private LocalDateTime dateTime;
}
