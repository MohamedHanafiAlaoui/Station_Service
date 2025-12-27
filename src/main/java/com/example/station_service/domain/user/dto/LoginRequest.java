package com.example.station_service.domain.user.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "username is required")
        String username,

        @NotBlank(message = "Password is required")
        String password
) {
}
