package com.example.station_service.domain.pompe.dto;

import com.example.station_service.domain.approvisionnementCarburant.entity.enums.TypeCarburant;
import com.example.station_service.infrastructure.validation.RegexPatterns;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PompeDto {

    private Long id;

    @NotBlank
    @Pattern(regexp = RegexPatterns.CODE_POMPE)
    private String codePompe;

    @NotNull
    private TypeCarburant typeCarburant;

    @NotNull
    @Positive
    private BigDecimal capaciteMax;

    @NotNull
    @PositiveOrZero
    private BigDecimal niveauActuel;

    @NotNull
    @Positive
    private BigDecimal prixParLitre;

    @NotNull
    private Boolean enService;

    @NotNull
    private Long stationId;
}
