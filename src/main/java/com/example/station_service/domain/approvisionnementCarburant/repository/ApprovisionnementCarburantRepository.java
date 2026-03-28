package com.example.station_service.domain.approvisionnementCarburant.repository;

import com.example.station_service.domain.approvisionnementCarburant.entity.ApprovisionnementCarburant;
import com.example.station_service.domain.approvisionnementCarburant.entity.enums.TypeCarburant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface ApprovisionnementCarburantRepository extends JpaRepository<ApprovisionnementCarburant, Long> {
    List<ApprovisionnementCarburant> findByDateApprovisionnementBetween(LocalDate start, LocalDate end);
    
    List<ApprovisionnementCarburant> findByStation_IdAndTypeCarburantAndQuantiteDisponibleGreaterThanOrderByDateApprovisionnementAsc(
            Long stationId, TypeCarburant typeCarburant, Double minQuantite);

    @Query("SELECT COALESCE(SUM(a.quantiteDisponible), 0.0) FROM ApprovisionnementCarburant a WHERE a.station.id = :stationId AND a.typeCarburant = :typeCarburant")
    Double sumQuantiteDisponible(@Param("stationId") Long stationId, @Param("typeCarburant") TypeCarburant typeCarburant);
}