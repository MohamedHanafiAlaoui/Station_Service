package com.example.station_service.domain.approvisionnementCarburant.repository;

import com.example.station_service.domain.approvisionnementCarburant.entity.ApprovisionnementCarburant;
import com.example.station_service.domain.approvisionnementCarburant.entity.enums.TypeCarburant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ApprovisionnementCarburantRepository extends JpaRepository<ApprovisionnementCarburant, Long> {

    List<ApprovisionnementCarburant> findByDateApprovisionnementBetween(LocalDate start, LocalDate end);
    List<ApprovisionnementCarburant> findByTypeCarburant(TypeCarburant typeCarburant);
    List<ApprovisionnementCarburant> findByTypeCarburantAndDateApprovisionnementBetween( TypeCarburant typeCarburant, LocalDate start, LocalDate end );
}
