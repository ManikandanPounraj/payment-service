package com.rak.payment.service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.rak.payment.client.BookingClient;
import com.rak.payment.client.NotificationClient;
import com.rak.payment.client.UserClient;
import com.rak.payment.model.BookingDTO;
import com.rak.payment.model.NotificationRequest;
import com.rak.payment.model.Payment;
import com.rak.payment.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

	private final PaymentRepository repo;
	private final UserClient userClient;
	private final BookingClient bookingClient;
	private final NotificationClient notificationClient;

	public Payment process(Long bookingId, double amount) {
		log.info("Processing payment for booking ID {}", bookingId);

		BookingDTO booking = bookingClient.getBooking(bookingId);
		userClient.validateUser(booking.getUserId());

		Payment payment = Payment.builder().bookingId(bookingId).userId(booking.getUserId()).amount(amount)
				.paymentTime(LocalDateTime.now()).build();

		Payment saved = repo.save(payment);
		log.info("Payment saved with ID {}", saved.getId());
		
		
		NotificationRequest notify = NotificationRequest.builder().userId(booking.getUserId())
				.eventId(booking.getEventId()).numberOfTickets(booking.getNumberOfSeats()).paymentAmount(amount)
				.type("BOOKING").build();

		notificationClient.sendNotification(notify);
		log.info("Notification triggered for payment");

		return saved;
	}

	public Payment get(Long id) {
		return repo.findById(id).orElseThrow(() -> new NoSuchElementException("Payment not found"));
	}
	
	public Payment refund(Long id) {
	    Payment payment = repo.findById(id)
	        .orElseThrow(() -> new NoSuchElementException("Payment not found"));

	    if (payment.isRefunded()) {
	        throw new IllegalStateException("Payment already refunded");
	    }

	    payment.setRefunded(true);
	    payment.setRefundTime(LocalDateTime.now());
	    return repo.save(payment);
	}

}
