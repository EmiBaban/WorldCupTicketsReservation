package com.mobylab.springbackend.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDto {
    private String category;
    private String message;
    private int rating;
    private boolean recommend;
    private LocalDateTime submittedAt;
}
