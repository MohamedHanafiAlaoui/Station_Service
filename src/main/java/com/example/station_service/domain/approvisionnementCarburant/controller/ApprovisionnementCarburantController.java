package com.example.station_service.domain.approvisionnementCarburant.controller;

import com.example.station_service.domain.approvisionnementCarburant.dto.ApprovisionnementCarburantDto;
import com.example.station_service.domain.approvisionnementCarburant.service.ApprovisionnementCarburantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/approvisionnements")
@RequiredArgsConstructor
public class ApprovisionnementCarburantController {

    private final ApprovisionnementCarburantService service;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApprovisionnementCarburantDto> create(@RequestBody ApprovisionnementCarburantDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    @PreAuthorize(
            "hasRole('ADMIN') or " +
                    "(hasRole('EMPLOYE') and @securityService.isUserOfStation(authentication, @approService.getStationIdByAppro(#id)))"
    )
    public ResponseEntity<ApprovisionnementCarburantDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ApprovisionnementCarburantDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/station/{stationId}")
    @PreAuthorize(
            "hasRole('ADMIN') or " +
                    "(hasRole('EMPLOYE') and @securityService.isUserOfStation(authentication, #stationId))"
    )
    public ResponseEntity<List<ApprovisionnementCarburantDto>> getByStation(@PathVariable Long stationId) {
        return ResponseEntity.ok(
                service.getAll().stream()
                        .filter(a -> a.getStationId().equals(stationId))
                        .toList()
        );
    }

    @GetMapping("/weekly")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ApprovisionnementCarburantDto>> getWeeklyReport() {
        return ResponseEntity.ok(service.getWeeklyReport());
    }

    @GetMapping("/monthly")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ApprovisionnementCarburantDto>> getMonthlyReport() {
        return ResponseEntity.ok(service.getMonthlyReport());
    }
}
