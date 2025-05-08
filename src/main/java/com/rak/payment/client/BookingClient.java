package com.rak.payment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.rak.payment.model.BookingDTO;

@FeignClient(name = "booking-service", url = "http://localhost:8083")
public interface BookingClient {
    @GetMapping("/api/bookings/{id}")
    BookingDTO getBooking(@PathVariable Long id);
}

