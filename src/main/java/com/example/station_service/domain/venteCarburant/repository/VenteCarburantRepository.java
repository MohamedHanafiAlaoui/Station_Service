package com.example.station_service.domain.venteCarburant.repository;
import com.example.station_service.domain.venteCarburant.entity.VenteCarburant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface VenteCarburantRepository extends JpaRepository<VenteCarburant, Long> {
    @EntityGraph(attributePaths = {"pompe", "client", "station"})
    @Query(value = "SELECT v FROM VenteCarburant v WHERE v.station.id = :stationId AND v.dateVente BETWEEN :start AND :end",
           countQuery = "SELECT count(v.id) FROM VenteCarburant v WHERE v.station.id = :stationId AND v.dateVente BETWEEN :start AND :end")
    Page<VenteCarburant> findByStationIdAndDateVenteBetween(
            @Param("stationId") Long stationId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable
    );
    @EntityGraph(attributePaths = {"pompe", "client", "station"})
    @Query(value = "SELECT v FROM VenteCarburant v WHERE v.station.id = :stationId AND v.pompe.id = :pompeId AND v.dateVente BETWEEN :start AND :end",
           countQuery = "SELECT count(v.id) FROM VenteCarburant v WHERE v.station.id = :stationId AND v.pompe.id = :pompeId AND v.dateVente BETWEEN :start AND :end")
    Page<VenteCarburant> findByStationIdAndPompeIdAndDateVenteBetween(
            @Param("stationId") Long stationId,
            @Param("pompeId") Long pompeId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable
    );
    @EntityGraph(attributePaths = {"pompe", "client", "station"})
    Page<VenteCarburant> findByClientIdAndDateVenteBetween(
            Long clientId,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );
    @Query("SELECT SUM(v.quantite) FROM VenteCarburant v WHERE v.station.id = :stationId AND v.dateVente BETWEEN :start AND :end")
    BigDecimal sumQuantiteByStationAndDateVenteBetween(@Param("stationId") Long stationId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    @Query("SELECT SUM(v.montantPaye) FROM VenteCarburant v WHERE v.station.id = :stationId AND v.dateVente BETWEEN :start AND :end")
    BigDecimal sumMontantByStationAndDateVenteBetween(@Param("stationId") Long stationId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}