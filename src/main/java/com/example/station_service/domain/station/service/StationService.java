package com.example.station_service.domain.station.service;

import com.example.station_service.domain.station.dto.StationDto;


import java.util.List;


public interface StationService {

     void createStation(StationDto dto);

      StationDto getStationById(Long id);

      List<StationDto> getAllStations();

      List<StationDto> getActiveStations();
      List<StationDto> getInactiveStations();

      long countActiveStations();

     List<StationDto> searchStations(String keyword);

      long countInactiveStations();

      List<StationDto> searchActiveStations(String keyword);

      StationDto updateStation(Long id, StationDto dto);

      void deleteStation(Long id,boolean active);


}
