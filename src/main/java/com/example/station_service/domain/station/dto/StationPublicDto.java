package com.example.station_service.domain.station.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StationPublicDto {
    private Long id;
    private String nom;
    private String adresse;
    private BigDecimal  latitude;
    private BigDecimal longitude;
}