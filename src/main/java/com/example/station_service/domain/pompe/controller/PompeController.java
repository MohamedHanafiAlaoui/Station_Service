package com.example.station_service.domain.pompe.controller;

import com.example.station_service.domain.pompe.dto.PompeDto;
import com.example.station_service.domain.pompe.service.PompeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pompes")
@RequiredArgsConstructor
public class PompeController {

    private final PompeService pompeService;

    @PostMapping
    public ResponseEntity<PompeDto> createPompe(@Validated @RequestBody PompeDto dto) {
        pompeService.createPompe(dto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PompeDto> getPompe(@PathVariable Long id) {
        PompeDto dto = pompeService.getPompeById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<PompeDto>> getAllPompes() {
        List<PompeDto> pompes = pompeService.getAllPompes();
        return ResponseEntity.ok(pompes);
    }

    @GetMapping("/active")
    public ResponseEntity<List<PompeDto>> getActivePompes() {
        List<PompeDto> pompes = pompeService.getActivePompes();
        return ResponseEntity.ok(pompes);
    }

    @GetMapping("/station/{stationId}")
    public ResponseEntity<List<PompeDto>> getPompesByStation(@PathVariable Long stationId) {
        List<PompeDto> pompes = pompeService.getPompesByStation(stationId);
        return ResponseEntity.ok(pompes);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PompeDto>> searchPompes(@RequestParam String keyword) {
        List<PompeDto> pompes = pompeService.searchPompes(keyword);
        return ResponseEntity.ok(pompes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PompeDto> updatePompe(@PathVariable Long id,
                                                @RequestBody PompeDto dto) {
        PompeDto updated = pompeService.updatePompe(id, dto);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> deletePompe(@PathVariable Long id,
                                            @RequestParam(defaultValue = "false") boolean enService) {
        pompeService.deletePompe(id, enService);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/sell")
    public ResponseEntity<PompeDto> sellFuel(@PathVariable Long id,
                                             @RequestParam double quantity) {
        PompeDto updated = pompeService.updatePompesell(id, quantity);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/add")
    public ResponseEntity<PompeDto> addFuel(@PathVariable Long id,
                                            @RequestParam double quantity) {
        PompeDto updated = pompeService.updatePompeAddNive(id, quantity);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/filter/niveau")
    public ResponseEntity<List<PompeDto>> filterByNiveau(@RequestParam double niveau) {
        List<PompeDto> pompes = pompeService.filterByNiveau(niveau);
        return ResponseEntity.ok(pompes);
    }

    @GetMapping("/count/active")
    public ResponseEntity<Long> countActivePompes() {
        long count = pompeService.countActivePompes();
        return ResponseEntity.ok(count);
    }
}
