package com.example.station_service.domain.venteCarburant.mapper;

import com.example.station_service.domain.venteCarburant.dto.VenteCarburantDto;
import com.example.station_service.domain.venteCarburant.entity.VenteCarburant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface VenteCarburantMapper {
    @Mapping(source = "pompe.id", target = "pompeId")
    @Mapping(source = "client.id", target = "clientId")
    VenteCarburantDto toDto(VenteCarburant entity);

    @Mapping(source = "pompeId", target = "pompe.id")
    @Mapping(source = "clientId", target = "client.id")
    VenteCarburant toEntity(VenteCarburantDto dto);
}
