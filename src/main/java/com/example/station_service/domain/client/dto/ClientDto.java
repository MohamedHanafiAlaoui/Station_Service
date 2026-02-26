package com.example.station_service.domain.client.dto;

import com.example.station_service.infrastructure.validation.RegexPatterns;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import java.math.BigDecimal;

@Getter
@Setter
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
    private BigDecimal solde;
}
