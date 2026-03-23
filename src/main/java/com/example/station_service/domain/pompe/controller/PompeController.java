package com.example.station_service.domain.pompe.controller;
import com.example.station_service.domain.pompe.dto.PompeDto;
import com.example.station_service.domain.pompe.service.PompeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/pompes")
@RequiredArgsConstructor
public class PompeController {
    private final PompeService pompeService;
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PompeDto> createPompe(@RequestBody PompeDto dto) {
        PompeDto created = pompeService.createPompe(dto);
        return ResponseEntity.ok(created);
    }
    @GetMapping("/{id}")
    @PreAuthorize(
            "hasRole('ADMIN') or " +
                    "(hasRole('EMPLOYE') and @securityService.isUserOfStation(authentication, @pompeService.getStationIdByPompe(#id)))"
    )
    public ResponseEntity<PompeDto> getPompe(@PathVariable Long id) {
        return ResponseEntity.ok(pompeService.getPompeById(id));
    }
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PompeDto>> getAllPompes() {
        return ResponseEntity.ok(pompeService.getAllPompes());
    }
    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYE')")
    public ResponseEntity<List<PompeDto>> getActivePompes() {
        return ResponseEntity.ok(pompeService.getActivePompes());
    }
    @GetMapping("/station/{stationId}")
    @PreAuthorize(
            "hasRole('ADMIN') or " +
                    "(hasRole('EMPLOYE') and @securityService.isUserOfStation(authentication, #stationId))"
    )
    public ResponseEntity<List<PompeDto>> getPompesByStation(@PathVariable Long stationId) {
        return ResponseEntity.ok(pompeService.getPompesByStation(stationId));
    }
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PompeDto>> searchPompes(@RequestParam String keyword) {
        return ResponseEntity.ok(pompeService.searchPompes(keyword));
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PompeDto> updatePompe(@PathVariable Long id, @RequestBody PompeDto dto) {
        return ResponseEntity.ok(pompeService.updatePompe(id, dto));
    }
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePompe(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean enService) {
        pompeService.deletePompe(id, enService);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}/add")
    @PreAuthorize(
            "hasRole('ADMIN') or " +
                    "(hasRole('EMPLOYE') and @securityService.isUserOfStation(authentication, @pompeService.getStationIdByPompe(#id)))"
    )
    public ResponseEntity<PompeDto> addFuel(@PathVariable Long id, @RequestParam double quantity) {
        System.out.println("--> CONTROLLER: addFuel request received. pompeId=" + id + ", quantity=" + quantity);
        PompeDto result = pompeService.updatePompeAddNive(id, quantity);
        System.out.println("<-- CONTROLLER: addFuel success.");
        return ResponseEntity.ok(result);
    }
    @GetMapping("/filter/niveau")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PompeDto>> filterByNiveau(@RequestParam double niveau) {
        return ResponseEntity.ok(pompeService.filterByNiveau(niveau));
    }
    @GetMapping("/count/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> countActivePompes() {
        return ResponseEntity.ok(pompeService.countActivePompes());
    }
}