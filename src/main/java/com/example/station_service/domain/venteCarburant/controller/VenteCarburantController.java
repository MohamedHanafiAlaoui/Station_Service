package com.example.station_service.domain.venteCarburant.controller;

import com.example.station_service.domain.venteCarburant.dto.VenteCarburantDto;
import com.example.station_service.domain.venteCarburant.service.VenteCarburantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/ventes")
@RequiredArgsConstructor
public class VenteCarburantController {

    private final VenteCarburantService venteService;



    @GetMapping("/station/{stationId}")
    public ResponseEntity<Page<VenteCarburantDto>> getVentesByStation(
            @PathVariable Long stationId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            Pageable pageable
    ) {
        return ResponseEntity.ok(venteService.getVentesByStationAndPeriod(stationId, start, end, pageable));
    }

    @GetMapping("/station/{stationId}/pompe/{pompeId}")
    public ResponseEntity<Page<VenteCarburantDto>> getVentesByStationAndPompe(
            @PathVariable Long stationId,
            @PathVariable Long pompeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            Pageable pageable
    ) {
        return ResponseEntity.ok(venteService.getVentesByStationAndPompeAndPeriod(stationId, pompeId, start, end, pageable));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<Page<VenteCarburantDto>> getVentesByClient(
            @PathVariable Long clientId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            Pageable pageable
    ) {
        return ResponseEntity.ok(venteService.getVentesByClientAndPeriod(clientId, start, end, pageable));
    }

    @GetMapping("/station/{stationId}/total-quantite")
    public ResponseEntity<BigDecimal> getTotalQuantite(
            @PathVariable Long stationId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        return ResponseEntity.ok(venteService.getTotalQuantiteByStationAndPeriod(stationId, start, end));
    }

    @GetMapping("/station/{stationId}/total-montant")
    public ResponseEntity<BigDecimal> getTotalMontant(
            @PathVariable Long stationId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        return ResponseEntity.ok(venteService.getTotalMontantByStationAndPeriod(stationId, start, end));
    }
}
