package com.example.station_service.domain.user.dto;

import com.example.station_service.domain.user.entity.enums.UserRole;
import com.example.station_service.infrastructure.validation.RegexPatterns;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class UserDto {
    private Long id;
    @NotBlank
    @Pattern(regexp = RegexPatterns.USERNAME)
    private String username;
    @NotBlank
    @Pattern(regexp = RegexPatterns.NAME)
    private String nom;
    @NotBlank
    @Pattern(regexp = RegexPatterns.NAME)
    private String prenom;
    @NotNull
    private Boolean actif;
    @NotNull
    private UserRole role;
}
