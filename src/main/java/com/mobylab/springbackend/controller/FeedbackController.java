package com.mobylab.springbackend.controller;


import com.mobylab.springbackend.service.FeedbackService;
import com.mobylab.springbackend.service.MatchService;
import com.mobylab.springbackend.service.dto.FeedbackDto;
import com.mobylab.springbackend.service.dto.MatchDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/v1/feedback")

public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/save")
    public ResponseEntity<?> saveFeedback(@RequestBody FeedbackDto dto) {
        feedbackService.create(dto);
        logger.info("Feedback saved");
        return ResponseEntity.ok(Collections.singletonMap("message", "Feedback saved"));
    }
}
