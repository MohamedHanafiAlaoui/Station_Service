package com.example.station_service.domain.station.repository;

import com.example.station_service.domain.station.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<Station,Long>
{
    List<Station> findByNom(String nom);
    List<Station> findByActiveTrue();
    List<Station> findByActiveFalse();
    long countByActiveTrue();
    long countByActiveFalse();
    List<Station> findByNomContainingIgnoreCase(String keyword);
    List<Station> findByNomContainingIgnoreCaseAndActiveTrue(String keyword);
}
