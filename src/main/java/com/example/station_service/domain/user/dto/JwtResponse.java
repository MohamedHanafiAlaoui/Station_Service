package com.example.station_service.domain.user.dto;


public record JwtResponse(
        String token,
        String type,
        String username,
        String roles,
        Long  id
) {
    public JwtResponse(String token, String username, String roles,Long id) {
        this(token, "Bearer", username, roles,id );
    }
}