package com.example.station_service.domain.user.dto;

import com.example.station_service.domain.user.entity.enums.UserRole;
import com.example.station_service.infrastructure.validation.RegexPatterns;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;




public record UserDto(
        @NotBlank
        @Pattern(regexp = RegexPatterns.USERNAME)
        String username,

        @NotBlank
        @Pattern(regexp = RegexPatterns.NAME)
        String nom,

        @NotBlank
        @Pattern(regexp = RegexPatterns.NAME)
        String prenom,

        Boolean actif,

        @NotNull
        UserRole role
        ,


        Long stationId,

        @NotNull
        String password


) {}