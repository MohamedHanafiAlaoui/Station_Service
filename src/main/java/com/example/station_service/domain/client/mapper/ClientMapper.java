package com.example.station_service.domain.client.mapper;

import com.example.station_service.domain.client.dto.ClientDto;
import com.example.station_service.domain.client.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    ClientDto toDto(Client client);

    @Mapping(target = "id", ignore = true)
    Client toEntity(ClientDto dto);
}
