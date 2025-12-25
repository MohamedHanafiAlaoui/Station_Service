package com.example.station_service.dto;

import com.example.station_service.entity.enums.MondePaiment;
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

    @NotNull
    private MondePaiment modePaiement;

    @NotBlank
    @Size(max = 30)
    private String statut;

    @NotNull
    private Long pompeId;

    @NotNull
    private Long employeId;

    private Long clientId;
}

