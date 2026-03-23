package com.example.station_service.domain.Employe.dto;
public record EmployeDto(
        Long id,
        Long stationId,
        String nom,
        String prenom,
        Boolean actif
) {}