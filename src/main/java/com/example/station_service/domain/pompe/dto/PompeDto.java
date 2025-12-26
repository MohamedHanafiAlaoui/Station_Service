package com.example.station_service.dto;

import com.example.station_service.domain.approvisionnementCarburant.entity.enums.TypeCarburant;
import com.example.station_service.infrastructure.validation.RegexPatterns;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PompeDto {

    private Long id;

    @NotBlank
    @Pattern(regexp = RegexPatterns.CODE_POMPE)
    private String codePompe;

    @NotNull
    private TypeCarburant typeCarburant;

    @Positive
    private double capaciteMax;

    @PositiveOrZero
    private double niveauActuel;

    @Positive
    private double prixParLitre;

    @NotNull
    private Boolean enService;

    @NotNull
    private Long stationId;
}
