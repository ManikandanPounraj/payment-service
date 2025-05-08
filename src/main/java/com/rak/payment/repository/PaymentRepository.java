package com.rak.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rak.payment.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
