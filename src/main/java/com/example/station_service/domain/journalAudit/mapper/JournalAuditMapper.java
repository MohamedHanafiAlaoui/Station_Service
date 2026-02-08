package com.example.station_service.domain.journalAudit.mapper;

import com.example.station_service.domain.journalAudit.dto.JournalAuditDto;
import com.example.station_service.domain.journalAudit.entity.JournalAudit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JournalAuditMapper {

    @Mapping(source = "station.id", target = "stationId")
    JournalAuditDto toDto(JournalAudit entity);

    @Mapping(source = "stationId", target = "station.id")
    JournalAudit toEntity(JournalAuditDto dto);
}
