package com.rak.payment;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.rak.payment.client.BookingClient;
import com.rak.payment.client.NotificationClient;
import com.rak.payment.client.UserClient;
import com.rak.payment.model.BookingDTO;
import com.rak.payment.model.Payment;
import com.rak.payment.repository.PaymentRepository;
import com.rak.payment.service.PaymentService;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    private PaymentRepository paymentRepo;
    private UserClient userClient;
    private BookingClient bookingClient;
    private NotificationClient notificationClient;
    private PaymentService service;

    @BeforeEach
    void setUp() {
        paymentRepo = mock(PaymentRepository.class);
        userClient = mock(UserClient.class);
        bookingClient = mock(BookingClient.class);
        notificationClient = mock(NotificationClient.class);

        service = new PaymentService(paymentRepo, userClient, bookingClient, notificationClient);
    }

    @Test
    void testProcessPayment_Success() {
        Long bookingId = 1L;
        double amount = 500;

        BookingDTO bookingDTO = new BookingDTO(bookingId, 1L, 2L, 2);
        Payment savedPayment = Payment.builder()
                .id(1L)
                .bookingId(bookingId)
                .userId(1L)
                .amount(amount)
                .paymentTime(LocalDateTime.now())
                .refunded(false)
                .build();

        doNothing().when(userClient).validateUser(1L);
        when(bookingClient.getBooking(bookingId)).thenReturn(bookingDTO);
        when(paymentRepo.save(any())).thenReturn(savedPayment);
        //doNothing().when(notificationClient).sendNotification("");

        Payment result = service.process(bookingId, amount);

        assertNotNull(result);
        assertEquals(bookingId, result.getBookingId());
        assertEquals(amount, result.getAmount());
    }

    @Test
    void testRefund_Success() {
        Payment payment = Payment.builder()
                .id(1L)
                .refunded(false)
                .build();

        when(paymentRepo.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentRepo.save(any())).thenReturn(payment);

        Payment result = service.refund(1L);
        assertTrue(result.isRefunded());
        assertNotNull(result.getRefundTime());
    }

    @Test
    void testRefund_AlreadyRefunded() {
        Payment payment = Payment.builder()
                .id(1L)
                .refunded(true)
                .build();

        when(paymentRepo.findById(1L)).thenReturn(Optional.of(payment));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> service.refund(1L));
        assertEquals("Payment already refunded", ex.getMessage());
    }

    @Test
    void testRefund_NotFound() {
        when(paymentRepo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> service.refund(99L));
    }

    @Test
    void testGetById_Success() {
        Payment payment = Payment.builder().id(1L).build();
        when(paymentRepo.findById(1L)).thenReturn(Optional.of(payment));

        Payment result = service.get(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetById_NotFound() {
        when(paymentRepo.findById(999L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> service.get(999L));
    }
}
