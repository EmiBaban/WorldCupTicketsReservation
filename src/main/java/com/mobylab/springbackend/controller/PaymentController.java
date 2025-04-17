package com.mobylab.springbackend.controller;

import com.mobylab.springbackend.service.PaymentService;
import com.mobylab.springbackend.service.dto.PaymentRequestDto;
import com.mobylab.springbackend.service.dto.PaymentResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController implements SecuredRestController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/checkout")
    public ResponseEntity<PaymentResponseDto> checkout(@RequestBody PaymentRequestDto request) {
        PaymentResponseDto response = paymentService.checkout(request);
        return ResponseEntity.ok(response);
    }

}
