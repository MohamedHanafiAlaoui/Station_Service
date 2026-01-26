package com.example.station_service.domain.pompe.mapper;


import com.example.station_service.domain.pompe.dto.PompeDto;
import com.example.station_service.domain.pompe.entity.Pompe;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PompeMapper {
    @Mapping(source = "stationId", target = "station.id")
    Pompe toEntity(PompeDto dto);
    @Mapping(source = "station.id", target = "stationId")
    PompeDto toDto(Pompe entity);
}
