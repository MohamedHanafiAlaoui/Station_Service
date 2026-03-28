package com.example.station_service.domain.user.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequest(
    @NotBlank
    String username,
    @NotBlank
    String oldPassword,
    @NotBlank
    String newPassword
) {}
