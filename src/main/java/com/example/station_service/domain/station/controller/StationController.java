package com.example.station_service.domain.station.controller;

import com.example.station_service.domain.station.dto.StationDto;
import com.example.station_service.domain.station.service.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        StationDto dto = stationService.getStationById(id);
        return ResponseEntity.ok(dto);
    }


    @GetMapping
    public ResponseEntity<List<StationDto>> getAllStations() {
        List<StationDto> stations = stationService.getAllStations();
        return ResponseEntity.ok(stations);
    }


    @GetMapping("/active")
    public ResponseEntity<List<StationDto>> getActiveStations() {
        List<StationDto> stations = stationService.getActiveStations();
        return ResponseEntity.ok(stations);
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<StationDto>> getInactiveStations() {
        List<StationDto> stations = stationService.getInactiveStations();
        return ResponseEntity.ok(stations);
    }


    @GetMapping("/search")
    public ResponseEntity<List<StationDto>> searchStations(@RequestParam String keyword) {
        List<StationDto> stations = stationService.searchStations(keyword);
        return ResponseEntity.ok(stations);
    }

    @GetMapping("/search/active")
    public ResponseEntity<List<StationDto>> searchActiveStations(@RequestParam String keyword) {
        List<StationDto> stations = stationService.searchActiveStations(keyword);
        return ResponseEntity.ok(stations);
    }


    @PutMapping("/{id}")
    public ResponseEntity<StationDto> updateStation(@PathVariable Long id,
                                                    @RequestBody StationDto dto) {
        StationDto updated = stationService.updateStation(id, dto);
        return ResponseEntity.ok(updated);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id,
                                              @RequestParam(defaultValue = "false") boolean active) {
        stationService.deleteStation(id, active);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/count/active")
    public ResponseEntity<Long> countActiveStations() {
        long count = stationService.countActiveStations();
        return ResponseEntity.ok(count);
    }


    @GetMapping("/count/inactive")
    public ResponseEntity<Long> countInactiveStations() {
        long count = stationService.countInactiveStations();
        return ResponseEntity.ok(count);
    }



}
