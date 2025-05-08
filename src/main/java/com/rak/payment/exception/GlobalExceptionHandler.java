package com.rak.payment.exception;

import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<?> handleNotFound(NoSuchElementException ex) {
		return ResponseEntity.status(404).body(Map.of("error", ex.getMessage()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGeneric(Exception ex) {
		return ResponseEntity.status(500).body(Map.of("error", "Internal error"));
	}
}
