package com.example.station_service.infrastructure.exception.custom;

public class StockInsufficientException extends RuntimeException {
    public StockInsufficientException(String message) {
        super(message);
    }
}