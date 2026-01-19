package com.example.station_service.domain.station.service;

import com.example.station_service.domain.station.dto.StationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



public abstract class StationService {

    public void createStation(StationDto dto){};

    public  StationDto getStationId(Long id){
        return null;
    };
    
}
