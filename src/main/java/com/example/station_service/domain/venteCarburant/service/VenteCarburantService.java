package com.example.station_service.domain.venteCarburant.service;
import com.example.station_service.domain.venteCarburant.dto.VenteCarburantDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
public interface VenteCarburantService {
    Page<VenteCarburantDto> getVentesByStationAndPeriod(Long stationId, LocalDate start, LocalDate end, Pageable pageable);
    Page<VenteCarburantDto> getVentesByStationAndPompeAndPeriod(Long stationId, Long pompeId, LocalDate start, LocalDate end, Pageable pageable);
    Page<VenteCarburantDto> getVentesByClientAndPeriod(Long clientId, LocalDate start, LocalDate end, Pageable pageable);
    BigDecimal getTotalQuantiteByStationAndPeriod(Long stationId, LocalDate start, LocalDate end);
    BigDecimal getTotalMontantByStationAndPeriod(Long stationId, LocalDate start, LocalDate end);
    VenteCarburantDto saveVente(VenteCarburantDto venteDTO);
    Page<VenteCarburantDto> getAllVentes(Pageable pageable);
}