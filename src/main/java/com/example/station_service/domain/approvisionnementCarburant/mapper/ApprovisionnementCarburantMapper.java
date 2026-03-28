package com.example.station_service.domain.approvisionnementCarburant.mapper;

import com.example.station_service.domain.approvisionnementCarburant.dto.ApprovisionnementCarburantDto;
import com.example.station_service.domain.approvisionnementCarburant.entity.ApprovisionnementCarburant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ApprovisionnementCarburantMapper {

    @Mapping(source = "station.id", target = "stationId")
    ApprovisionnementCarburantDto toDto(ApprovisionnementCarburant entity);

    @Mapping(source = "stationId", target = "station.id")
    ApprovisionnementCarburant toEntity(ApprovisionnementCarburantDto dto);
}