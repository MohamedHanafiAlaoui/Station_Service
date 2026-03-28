package com.example.station_service.domain.pompe.repository;
import com.example.station_service.domain.approvisionnementCarburant.entity.enums.TypeCarburant;
import com.example.station_service.domain.pompe.entity.Pompe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PompeRepository extends JpaRepository<Pompe,Long> {
    List<Pompe> findByEnServiceTrue();
    List<Pompe> findByStation_Id(Long stationId);
    List<Pompe> findByNiveauActuelGreaterThan(double niveau);
    Optional<Pompe> findTopByOrderByIdDesc();



}