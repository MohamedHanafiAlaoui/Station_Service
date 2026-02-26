package com.example.station_service.domain.journalAudit.service;

import com.example.station_service.domain.journalAudit.dto.JournalAuditDto;
import com.example.station_service.domain.journalAudit.entity.JournalAudit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface JournalAuditService {


    Page<JournalAuditDto> getAllJournal(Pageable pageable);

    Page<JournalAuditDto> getByStationAndPeriod(Long stationId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<JournalAuditDto> getByPeriod(LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<JournalAuditDto> getByTypeActionAndPeriod(String typeAction, LocalDateTime start, LocalDateTime end, Pageable pageable);

    void createJournal(JournalAuditDto dto);
}
