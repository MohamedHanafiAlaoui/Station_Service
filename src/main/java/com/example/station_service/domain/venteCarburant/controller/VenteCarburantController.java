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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.math.BigDecimal;
@RestController
@RequestMapping("/api/ventes")
@RequiredArgsConstructor
public class VenteCarburantController {
    private final VenteCarburantService venteService;
    @GetMapping("/station/{stationId}")
    @PreAuthorize(
            "hasRole('ADMIN') or " +
                    "(hasRole('EMPLOYE') and @securityService.isUserOfStation(authentication, #stationId))"
    )
    public ResponseEntity<Page<VenteCarburantDto>> getVentesByStation(
            @PathVariable Long stationId,
            @RequestParam String start,
            @RequestParam String end,
            Pageable pageable
    ) {
        LocalDate startDate = LocalDate.parse(start.substring(0, 10));
        LocalDate endDate = LocalDate.parse(end.substring(0, 10));
        return ResponseEntity.ok(
                venteService.getVentesByStationAndPeriod(stationId, startDate, endDate, pageable)
        );
    }
    @GetMapping("/station/{stationId}/pompe/{pompeId}")
    @PreAuthorize(
            "hasRole('ADMIN') or " +
                    "(hasRole('EMPLOYE') and @securityService.isUserOfStation(authentication, #stationId))"
    )
    public ResponseEntity<Page<VenteCarburantDto>> getVentesByStationAndPompe(
            @PathVariable Long stationId,
            @PathVariable Long pompeId,
            @RequestParam String start,
            @RequestParam String end,
            Pageable pageable
    ) {
        LocalDate startDate = LocalDate.parse(start.substring(0, 10));
        LocalDate endDate = LocalDate.parse(end.substring(0, 10));
        return ResponseEntity.ok(
                venteService.getVentesByStationAndPompeAndPeriod(stationId, pompeId, startDate, endDate, pageable)
        );
    }
    @GetMapping("/client/{clientId}")
    @PreAuthorize(
            "hasRole('CLIENT') and @securityService.isClientOwner(authentication, #clientId)"
    )
    public ResponseEntity<Page<VenteCarburantDto>> getVentesByClient(
            @PathVariable Long clientId,
            @RequestParam String start,
            @RequestParam String end,
            Pageable pageable
    ) {
        LocalDate startDate = LocalDate.parse(start.substring(0, 10));
        LocalDate endDate = LocalDate.parse(end.substring(0, 10));
        return ResponseEntity.ok(
                venteService.getVentesByClientAndPeriod(clientId, startDate, endDate, pageable)
        );
    }
    @GetMapping("/station/{stationId}/total-quantite")
    @PreAuthorize(
            "hasRole('ADMIN') or " +
                    "(hasRole('EMPLOYE') and @securityService.isUserOfStation(authentication, #stationId))"
    )
    public ResponseEntity<BigDecimal> getTotalQuantite(
            @PathVariable Long stationId,
            @RequestParam String start,
            @RequestParam String end
    ) {
        LocalDate startDate = LocalDate.parse(start.substring(0, 10));
        LocalDate endDate = LocalDate.parse(end.substring(0, 10));
        return ResponseEntity.ok(
                venteService.getTotalQuantiteByStationAndPeriod(stationId, startDate, endDate)
        );
    }
    @GetMapping("/station/{stationId}/total-montant")
    @PreAuthorize(
            "hasRole('ADMIN') or " +
                    "(hasRole('EMPLOYE') and @securityService.isUserOfStation(authentication, #stationId))"
    )
    public ResponseEntity<BigDecimal> getTotalMontant(
            @PathVariable Long stationId,
            @RequestParam String start,
            @RequestParam String end
    ) {
        LocalDate startDate = LocalDate.parse(start.substring(0, 10));
        LocalDate endDate = LocalDate.parse(end.substring(0, 10));
        return ResponseEntity.ok(
                venteService.getTotalMontantByStationAndPeriod(stationId, startDate, endDate)
        );
    }
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<VenteCarburantDto>> getAllVentes(Pageable pageable) {
        return ResponseEntity.ok(venteService.getAllVentes(pageable));
    }
}