package com.example.station_service.repository;

import com.example.station_service.entity.ApprovisionnementCarburant;
import com.example.station_service.entity.enums.TypeCarburant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovisionnementCarburantRepository extends JpaRepository<ApprovisionnementCarburant,Long> {

    List<ApprovisionnementCarburant> findByStationId(Long stationId);

    List<ApprovisionnementCarburant> findByTypeCarburant(TypeCarburant typeCarburant);
}
