package com.rak.payment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationRequest {
    private Long userId;
    private Long eventId;
    private int numberOfTickets;
    private double paymentAmount;
    private String type; // BOOKING
}
