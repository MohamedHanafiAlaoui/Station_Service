package com.example.station_service.domain.station.mapper;

import com.example.station_service.domain.station.dto.StationDto;
import com.example.station_service.domain.station.entity.Station;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")

public interface StationMapper {

 StationDto toDto(Station station);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "pompes", ignore = true)
    @Mapping(target = "employes", ignore = true)
 Station toEntity(StationDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "pompes", ignore = true)
    @Mapping(target = "employes", ignore = true)
    void updateEntityFromDto(StationDto dto, @MappingTarget Station station);
}
