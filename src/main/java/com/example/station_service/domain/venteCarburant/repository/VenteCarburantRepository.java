package com.example.station_service.domain.venteCarburant.repository;

import com.example.station_service.domain.venteCarburant.entity.VenteCarburant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Repository
public interface VenteCarburantRepository extends JpaRepository<VenteCarburant, Long> {

    Page<VenteCarburant> findByStation_IdAndDateVenteBetween(
            Long stationId,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );

    Page<VenteCarburant> findByStation_IdAndPompe_IdAndDateVenteBetween(
            Long stationId,
            Long pompeId,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );

    Page<VenteCarburant> findByClientIdAndDateVenteBetween(
            Long clientId,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );

    @Query("SELECT SUM(v.quantite) FROM VenteCarburant v WHERE v.station.id = :stationId AND v.dateVente BETWEEN :start AND :end")
    BigDecimal sumQuantiteByStationAndDateVenteBetween(Long stationId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT SUM(v.montantPaye) FROM VenteCarburant v WHERE v.station.id = :stationId AND v.dateVente BETWEEN :start AND :end")
    BigDecimal sumMontantByStationAndDateVenteBetween(Long stationId, LocalDateTime start, LocalDateTime end);

}
