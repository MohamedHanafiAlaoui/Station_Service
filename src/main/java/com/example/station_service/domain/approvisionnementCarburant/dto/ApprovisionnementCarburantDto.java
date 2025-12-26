package com.example.station_service.domain.approvisionnementCarburant.dto;

import com.example.station_service.domain.approvisionnementCarburant.entity.enums.TypeCarburant;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ApprovisionnementCarburantDto {

    private Long id;

    @Positive
    private Double quantite;

    @NotNull
    private TypeCarburant typeCarburant;

    @NotNull
    private Long stationId;
}
