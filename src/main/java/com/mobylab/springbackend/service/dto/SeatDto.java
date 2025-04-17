package com.mobylab.springbackend.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatDto {
    private UUID id;
    private String row;
    private int number;
    private String status;
}