package com.example.station_service.repository;

import com.example.station_service.entity.enums.MondePaiment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VenteCarburantRepository extends JpaRepository<VenteCarburant,Long> {
    List<VenteCarburant> findByPompeId(Long pompeId);

    List<VenteCarburant> findByEmployeId(Long employeId);

    List<VenteCarburant> findByClientId(Long clientId);

    List<VenteCarburant> findByModePaiement(MondePaiment modePaiement);
}
