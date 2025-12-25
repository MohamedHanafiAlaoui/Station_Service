package com.example.station_service.dto;

import com.example.station_service.infrastructure.validation.RegexPatterns;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class ClientDto {
    private Long id;
    @NotBlank
    @Pattern(regexp = RegexPatterns.USERNAME)
    private String nom;
    @NotBlank
    @Pattern(regexp = RegexPatterns.USERNAME)
    private String prenom;
    @NotBlank
    @Pattern(regexp = RegexPatterns.RFID)
    private String badgeRFID;
    @PositiveOrZero
    private double solde;
}
