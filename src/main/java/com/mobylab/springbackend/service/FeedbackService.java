package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.Feedback;
import com.mobylab.springbackend.entity.User;
import com.mobylab.springbackend.repository.FeedbackRepository;
import com.mobylab.springbackend.repository.UserRepository;
import com.mobylab.springbackend.service.dto.FeedbackDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    public void create(FeedbackDto dto) {
        Feedback feedback = new Feedback();
        feedback.setCategory(dto.getCategory());
        feedback.setMessage(dto.getMessage());
        feedback.setRating(dto.getRating());
        feedback.setRecommend(dto.isRecommend());
        feedback.setSubmittedAt(LocalDateTime.now());

        feedbackRepository.save(feedback);
    }
}
