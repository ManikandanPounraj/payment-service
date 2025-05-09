package com.rak.payment;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.rak.payment.controller.PaymentController;
import com.rak.payment.model.Payment;
import com.rak.payment.service.PaymentService;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentControllerTest {

    private PaymentService paymentService;
    private PaymentController paymentController;

    @BeforeEach
    void setUp() {
        paymentService = mock(PaymentService.class);
        paymentController = new PaymentController(paymentService);
    }

    @Test
    void testProcessPayment() {
        Payment payment = Payment.builder()
                .id(1L)
                .bookingId(100L)
                .amount(500.0)
                .paymentTime(LocalDateTime.now())
                .build();

        when(paymentService.process(100L, 500.0)).thenReturn(payment);

        ResponseEntity<Payment> response = paymentController.process(100L, 500.0);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(100L, response.getBody().getBookingId());
    }

    @Test
    void testGetPaymentById_Success() {
        Payment payment = Payment.builder().id(1L).amount(500.0).build();
        when(paymentService.get(1L)).thenReturn(payment);

        ResponseEntity<Payment> response = paymentController.get(1L);
        assertEquals(500.0, response.getBody().getAmount());
    }

    @Test
    void testGetPaymentById_NotFound() {
        when(paymentService.get(999L)).thenThrow(new NoSuchElementException("Not found"));
        assertThrows(NoSuchElementException.class, () -> paymentController.get(999L));
    }

    @Test
    void testRefundPayment() {
        Payment refunded = Payment.builder()
                .id(1L)
                .refunded(true)
                .refundTime(LocalDateTime.now())
                .build();

        when(paymentService.refund(1L)).thenReturn(refunded);

        ResponseEntity<Payment> response = paymentController.refund(1L);
        assertTrue(response.getBody().isRefunded());
        assertEquals(200, response.getStatusCodeValue());
    }
}
