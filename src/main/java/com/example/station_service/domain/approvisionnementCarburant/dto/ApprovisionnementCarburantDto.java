package com.example.station_service.domain.approvisionnementCarburant.dto;

import com.example.station_service.domain.approvisionnementCarburant.entity.enums.TypeCarburant;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApprovisionnementCarburantDto {

    private Long id;

    @NotNull
    @Positive(message = "La quantité doit être positive")
    private Double quantite;

    @NotNull(message = "Le type de carburant est obligatoire")
    private TypeCarburant typeCarburant;

    @NotNull(message = "L'identifiant de la station est obligatoire")
    private Long stationId;
}
