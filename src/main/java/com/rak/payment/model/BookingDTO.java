package com.rak.payment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDTO {
	private Long id;
	private Long userId;
	private Long eventId;
	private int numberOfSeats;
}
