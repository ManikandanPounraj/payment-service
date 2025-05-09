package com.rak.payment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rak.payment.model.Payment;
import com.rak.payment.service.PaymentService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

	private final PaymentService service;

	@PostMapping
	@Operation(summary = "Process payment for booking")
	public ResponseEntity<Payment> process(@RequestParam Long bookingId, @RequestParam double amount) {
		return ResponseEntity.ok(service.process(bookingId, amount));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get payment by ID")
	public ResponseEntity<Payment> get(@PathVariable Long id) {
		return ResponseEntity.ok(service.get(id));
	}
	
	@PostMapping("/{id}/refund")
	@Operation(summary = "Refund a payment")
	public ResponseEntity<Payment> refund(@PathVariable Long id) {
	    return ResponseEntity.ok(service.refund(id));
	}

}
