package com.example.station_service.domain.station.service;

import com.example.station_service.domain.station.dto.StationDto;
import com.example.station_service.domain.station.entity.Station;
import com.example.station_service.domain.station.mapper.StationMapper;
import com.example.station_service.domain.station.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StationServiceImpl implements   StationService{


    private final StationRepository stationRepository;
    private final StationMapper stationMapper;

    @Override
    public  void createStation(StationDto dto)
    {
        Station entity = stationMapper.toEntity(dto);
        stationRepository.save(entity);

    }
    @Override
    public StationDto getStationId(Long id) {
        Station station =stationRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("not found " + id));

        return stationMapper.toDto(station);
    }



}
