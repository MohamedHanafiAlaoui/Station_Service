package com.example.station_service.domain.journalAudit.repository;

import com.example.station_service.domain.journalAudit.entity.JournalAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JournalAuditRepository extends JpaRepository<JournalAudit,Long>
{
    List<JournalAudit> findByStationId(Long stationId);

}
