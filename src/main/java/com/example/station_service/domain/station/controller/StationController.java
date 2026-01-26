package com.example.station_service.domain.station.controller;

import com.example.station_service.domain.station.dto.StationDto;
import com.example.station_service.domain.station.service.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stations")
@RequiredArgsConstructor
public class StationController {
    private  final StationService stationService;

    @PostMapping
    public ResponseEntity<StationDto> createStation(@Validated @RequestBody StationDto dto) {
        stationService.createStation(dto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StationDto> getStation(@PathVariable Long id) {
        StationDto dto = stationService.getStationId(id);
        return ResponseEntity.ok(dto);
    }
}
