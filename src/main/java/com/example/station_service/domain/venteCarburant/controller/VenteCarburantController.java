package com.example.station_service.domain.venteCarburant.controller;

import com.example.station_service.domain.venteCarburant.dto.VenteCarburantDto;
import com.example.station_service.domain.venteCarburant.service.VenteCarburantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/ventes")
@RequiredArgsConstructor
public class VenteCarburantController {

    private final VenteCarburantService venteService;

    // ADMIN + EMPLOYE (محطته فقط)
    @GetMapping("/station/{stationId}")
    @PreAuthorize(
            "hasRole('ADMIN') or " +
                    "(hasRole('EMPLOYE') and @securityService.isUserOfStation(authentication, #stationId))"
    )
    public ResponseEntity<Page<VenteCarburantDto>> getVentesByStation(
            @PathVariable Long stationId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                venteService.getVentesByStationAndPeriod(stationId, start, end, pageable)
        );
    }

    // ADMIN + EMPLOYE (محطته فقط)
    @GetMapping("/station/{stationId}/pompe/{pompeId}")
    @PreAuthorize(
            "hasRole('ADMIN') or " +
                    "(hasRole('EMPLOYE') and @securityService.isUserOfStation(authentication, #stationId))"
    )
    public ResponseEntity<Page<VenteCarburantDto>> getVentesByStationAndPompe(
            @PathVariable Long stationId,
            @PathVariable Long pompeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                venteService.getVentesByStationAndPompeAndPeriod(stationId, pompeId, start, end, pageable)
        );
    }

    // CLIENT → يرى مبيعاته فقط
    @GetMapping("/client/{clientId}")
    @PreAuthorize(
            "hasRole('CLIENT') and @securityService.isClientOwner(authentication, #clientId)"
    )
    public ResponseEntity<Page<VenteCarburantDto>> getVentesByClient(
            @PathVariable Long clientId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                venteService.getVentesByClientAndPeriod(clientId, start, end, pageable)
        );
    }

    // ADMIN + EMPLOYE (محطته فقط)
    @GetMapping("/station/{stationId}/total-quantite")
    @PreAuthorize(
            "hasRole('ADMIN') or " +
                    "(hasRole('EMPLOYE') and @securityService.isUserOfStation(authentication, #stationId))"
    )
    public ResponseEntity<BigDecimal> getTotalQuantite(
            @PathVariable Long stationId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        return ResponseEntity.ok(
                venteService.getTotalQuantiteByStationAndPeriod(stationId, start, end)
        );
    }

    // ADMIN + EMPLOYE (محطته فقط)
    @GetMapping("/station/{stationId}/total-montant")
    @PreAuthorize(
            "hasRole('ADMIN') or " +
                    "(hasRole('EMPLOYE') and @securityService.isUserOfStation(authentication, #stationId))"
    )
    public ResponseEntity<BigDecimal> getTotalMontant(
            @PathVariable Long stationId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        return ResponseEntity.ok(
                venteService.getTotalMontantByStationAndPeriod(stationId, start, end)
        );
    }
}
