package com.example.station_service.domain.station.service;

import com.example.station_service.domain.station.dto.StationDto;
import com.example.station_service.domain.station.dto.StationPublicDto;

import java.util.List;

public interface StationService {

 void createStation(StationDto dto);

 StationDto getStationById(Long id);

 List<StationDto> getAllStations();

 List<StationDto> getActiveStations();

 List<StationDto> getInactiveStations();

 long countActiveStations();

 long countInactiveStations();

 List<StationDto> searchStations(String keyword);

 List<StationDto> searchActiveStations(String keyword);

 StationDto updateStation(Long id, StationDto dto);

 void deleteStation(Long id, boolean active);

 List<StationPublicDto> getAllStationsPublic();

}
