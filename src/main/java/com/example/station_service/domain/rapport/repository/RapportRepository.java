package com.example.station_service.repository;

import com.example.station_service.entity.enums.TypeRapport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RapportRepository extends JpaRepository<Rapport,Long> {
    List<Rapport> findByEmployeId(Long employeId);

    List<Rapport> findByType(TypeRapport type);
}
