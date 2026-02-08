package com.example.station_service.domain.journalAudit.repository;

import com.example.station_service.domain.journalAudit.entity.JournalAudit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface JournalAuditRepository extends JpaRepository<JournalAudit, Long> {

    Page<JournalAudit> findByStation_IdAndDateActionBetween(
            Long stationId,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );

    Page<JournalAudit> findByDateActionBetween(
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );

    Page<JournalAudit> findByTypeActionAndDateActionBetween(
            String typeAction,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );

}
