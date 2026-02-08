package com.example.station_service.domain.approvisionnementCarburant.mapper;

import com.example.station_service.domain.approvisionnementCarburant.dto.ApprovisionnementCarburantDto;
import com.example.station_service.domain.approvisionnementCarburant.entity.ApprovisionnementCarburant;

import com.example.station_service.domain.journalAudit.dto.JournalAuditDto;
import com.example.station_service.domain.journalAudit.entity.JournalAudit;
import com.example.station_service.domain.station.entity.Station;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ApprovisionnementCarburantMapper {

    @Mapping(source = "station.id", target = "stationId")
    ApprovisionnementCarburantDto toDto(ApprovisionnementCarburant entity);

    @Mapping(target = "station", ignore = true)
    ApprovisionnementCarburant toEntity(ApprovisionnementCarburantDto dto);
    @AfterMapping
    default void setStation(JournalAuditDto dto, @MappingTarget JournalAudit entity) { if (dto.getStationId() != null)
    {
        Station station = new Station();
        station.setId(dto.getStationId());
        entity.setStation(station); }
    }
}
