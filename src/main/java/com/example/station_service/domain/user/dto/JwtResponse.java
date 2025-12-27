package com.example.station_service.domain.user.dto;


public record JwtResponse(
        String token,
        String type,
        String username,
        String roles
) {
    public JwtResponse(String token, String username, String roles) {
        this(token, "Bearer", username, roles );
    }
}