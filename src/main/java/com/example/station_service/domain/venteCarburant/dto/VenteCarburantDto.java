package com.example.station_service.domain.venteCarburant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VenteCarburantDto {

    private Long id;

    @Positive
    private Double montantPaye;

    @Positive
    private Double quantite;

    @Positive
    private Double prixUnitaire;


    @NotBlank
    @Size(max = 30)
    private String statut;

    @NotNull
    private Long pompeId;


    private Long clientId;
}

