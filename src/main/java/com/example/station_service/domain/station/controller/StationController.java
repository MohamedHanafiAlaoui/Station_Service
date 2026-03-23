package com.example.station_service.domain.station.controller;
import com.example.station_service.domain.station.dto.StationDto;
import com.example.station_service.domain.station.dto.StationPublicDto;
import com.example.station_service.domain.station.service.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/stations")
@RequiredArgsConstructor
public class StationController {
    private final StationService stationService;
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StationDto> createStation(@Validated @RequestBody StationDto dto) {
        stationService.createStation(dto);
        return ResponseEntity.ok(dto);
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('EMPLOYE') and @securityService.isUserOfStation(authentication, #id))")
    public ResponseEntity<StationDto> getStation(@PathVariable Long id) {
        StationDto dto = stationService.getStationById(id);
        return ResponseEntity.ok(dto);
    }
    @GetMapping("/public")
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN') or hasRole('EMPLOYE')")
    public ResponseEntity<List<StationPublicDto>> getAllStationsPublic() {
        return ResponseEntity.ok(stationService.getAllStationsPublic());
    }
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StationDto>> getAllStations() {
        return ResponseEntity.ok(stationService.getAllStations());
    }
    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StationDto>> getActiveStations() {
        return ResponseEntity.ok(stationService.getActiveStations());
    }
    @GetMapping("/inactive")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StationDto>> getInactiveStations() {
        return ResponseEntity.ok(stationService.getInactiveStations());
    }
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StationDto>> searchStations(@RequestParam String keyword) {
        return ResponseEntity.ok(stationService.searchStations(keyword));
    }
    @GetMapping("/search/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StationDto>> searchActiveStations(@RequestParam String keyword) {
        return ResponseEntity.ok(stationService.searchActiveStations(keyword));
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StationDto> updateStation(@PathVariable Long id, @RequestBody StationDto dto) {
        return ResponseEntity.ok(stationService.updateStation(id, dto));
    }
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean active) {
        stationService.deleteStation(id, active);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/count/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> countActiveStations() {
        return ResponseEntity.ok(stationService.countActiveStations());
    }
    @GetMapping("/count/inactive")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> countInactiveStations() {
        return ResponseEntity.ok(stationService.countInactiveStations());
    }
}