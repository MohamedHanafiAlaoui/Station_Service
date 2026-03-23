package com.example.station_service.domain.station.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;
@Data
public class StationDto {
    private Long id;
    @NotBlank
    @Size(min = 3, max = 100)
    private String nom;
    @NotBlank
    @Size(min = 5, max = 100)
    private String adresse;
    private boolean active;
    private BigDecimal  latitude;
    private BigDecimal longitude;
}