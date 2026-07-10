package com.sporekart.payment.application.service;

import com.sporekart.payment.domain.model.Payment;
import com.sporekart.payment.domain.model.PaymentStatus;
import com.sporekart.payment.infrastructure.persistence.InMemoryPaymentRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PaymentServiceTest {
    @Test
    void initiateAndCapturePayment() {
        PaymentService service = new PaymentService(new InMemoryPaymentRepository());
        Payment payment = service.initiate("order-1", new BigDecimal("199.99"), "UPI");

        assertNotNull(payment);
        assertEquals(PaymentStatus.INITIATED, payment.getStatus());

        Payment captured = service.capture(payment.getId());
        assertEquals(PaymentStatus.CAPTURED, captured.getStatus());
    }
}
