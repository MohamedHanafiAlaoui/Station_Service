package com.example.station_service.domain.journalAudit.service;

import com.example.station_service.domain.journalAudit.dto.JournalAuditDto;
import com.example.station_service.domain.journalAudit.entity.JournalAudit;
import com.example.station_service.domain.journalAudit.mapper.JournalAuditMapper;
import com.example.station_service.domain.journalAudit.repository.JournalAuditRepository;
import com.example.station_service.domain.pompe.entity.Pompe;
import com.example.station_service.domain.station.entity.Station;
import com.example.station_service.domain.station.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class JournalAuditServiceImpl implements JournalAuditService {

    private final JournalAuditRepository journalAuditRepository;
    private final StationRepository stationRepository;
    private final JournalAuditMapper mapper;

    @Override
    public Page<JournalAuditDto> getAllJournal(Pageable pageable) {
        return journalAuditRepository.findAll(pageable)
                .map(mapper::toDto);
    }

    @Override
    public Page<JournalAuditDto> getByStationAndPeriod(Long stationId, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return journalAuditRepository.findByStation_IdAndDateActionBetween(
                stationId,
                start,
                end,
                pageable
        ).map(mapper::toDto);
    }

    @Override
    public Page<JournalAuditDto> getByPeriod(LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return journalAuditRepository.findByDateActionBetween(start, end, pageable)
                .map(mapper::toDto);
    }

    @Override
    public Page<JournalAuditDto> getByTypeActionAndPeriod(String typeAction, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return journalAuditRepository.findByTypeActionAndDateActionBetween(typeAction, start, end, pageable)
                .map(mapper::toDto);
    }

    @Override
    public void createJournal(JournalAuditDto dto)
    {

        JournalAudit journalAudit = mapper.toEntity(dto);

        journalAuditRepository.save(journalAudit);
    }
}
