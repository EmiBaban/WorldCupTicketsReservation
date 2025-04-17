package com.mobylab.springbackend.service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class PaymentResponseDto {
    private UUID paymentId;
    private double totalAmount;
    private int ticketCount;
}
