package com.example.station_service.dto;

import com.example.station_service.entity.enums.TypeRapport;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class RapportDto {

    private Long id;

    @NotNull
    private TypeRapport type;

    @NotBlank
    @Size(min = 10, max = 5000)
    private String donnees;

    @NotNull
    private Long employeId;
}

