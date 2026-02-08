package com.example.station_service.domain.journalAudit.service;

import com.example.station_service.domain.journalAudit.entity.JournalAudit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface JournalAuditService {

    Page<JournalAudit> getAllJournal(Pageable pageable);

    Page<JournalAudit> getByStationAndPeriod(Long stationId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<JournalAudit> getByPeriod(LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<JournalAudit> getByTypeActionAndPeriod(String typeAction, LocalDateTime start, LocalDateTime end, Pageable pageable);

    JournalAudit createJournal(JournalAudit journalAudit);
}
