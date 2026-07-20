package com.sporekart.payment.interfaces.rest;

import com.sporekart.payment.application.service.PaymentService;
import com.sporekart.payment.domain.model.Payment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create-order")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Payment> createOrder(@RequestBody CreatePaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.initiate(request.orderId(), request.amount(), request.paymentMethod()));
    }

    @PostMapping("/{paymentId}/capture")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Payment> capture(@PathVariable String paymentId) {
        return ResponseEntity.ok(paymentService.capture(paymentId));
    }

    @PostMapping("/{paymentId}/fail")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Payment> fail(@PathVariable String paymentId) {
        return ResponseEntity.ok(paymentService.fail(paymentId));
    }

    public record CreatePaymentRequest(String orderId, BigDecimal amount, String paymentMethod) {
    }
}
