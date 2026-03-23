package com.example.station_service.domain.venteCarburant.mapper;
import com.example.station_service.domain.venteCarburant.dto.VenteCarburantDto;
import com.example.station_service.domain.venteCarburant.entity.VenteCarburant;
import com.example.station_service.domain.client.mapper.ClientMapper;
import com.example.station_service.domain.pompe.mapper.PompeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
@Mapper(componentModel = "spring", 
        uses = {PompeMapper.class, ClientMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
public interface VenteCarburantMapper {
    @Mapping(target = "pompeId", source = "pompe.id")
    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "montant", source = "montantPaye")
    VenteCarburantDto toDto(VenteCarburant entity);
    @Mapping(source = "montant", target = "montantPaye")
    @Mapping(target = "station", ignore = true)
    @Mapping(target = "pompe", ignore = true)
    @Mapping(target = "client", ignore = true)
    VenteCarburant toEntity(VenteCarburantDto dto);
}