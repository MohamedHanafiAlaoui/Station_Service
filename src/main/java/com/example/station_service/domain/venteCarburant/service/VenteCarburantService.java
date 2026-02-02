package com.example.station_service.domain.venteCarburant.service;

import com.example.station_service.domain.venteCarburant.dto.VenteCarburantDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface VenteCarburantService {

    Page<VenteCarburantDto> getVentesByStationAndPeriod(Long stationId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<VenteCarburantDto> getVentesByStationAndPompeAndPeriod(Long stationId, Long pompeId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<VenteCarburantDto> getVentesByClientAndPeriod(Long clientId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    BigDecimal getTotalQuantiteByStationAndPeriod(Long stationId, LocalDateTime start, LocalDateTime end);

    BigDecimal getTotalMontantByStationAndPeriod(Long stationId, LocalDateTime start, LocalDateTime end);

    VenteCarburantDto saveVente(VenteCarburantDto venteDTO);

}
