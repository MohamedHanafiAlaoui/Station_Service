package com.example.station_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PompeRepository extends JpaRepository<Pompe,Long> {
    List<Pompe> findByEnServiceTrue();

    List<Pompe> findByStation_Id(Long stationId);

    List<Pompe> findByNiveauActuelGreaterThan(double niveau);

}
