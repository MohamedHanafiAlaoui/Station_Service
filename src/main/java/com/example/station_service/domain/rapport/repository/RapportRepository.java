package com.example.station_service.domain.rapport.repository;

import com.example.station_service.domain.rapport.entity.Rapport;
import com.example.station_service.domain.rapport.entity.enums.TypeRapport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RapportRepository extends JpaRepository<Rapport,Long> {
    List<Rapport> findByEmployeId(Long employeId);

    List<Rapport> findByType(TypeRapport type);
}
