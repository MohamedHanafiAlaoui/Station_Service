package com.example.station_service.domain.user.dto;
public record JwtResponse(
        String token,
        String type,
        String username,
        String roles,
        Long id,
        Long stationId
) {
    public JwtResponse(String token, String username, String roles, Long id, Long stationId) {
        this(token, "Bearer", username, roles, id, stationId);
    }
}