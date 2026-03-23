package com.example.station_service.domain.journalAudit.service;
import com.example.station_service.domain.journalAudit.dto.JournalAuditDto;
import com.example.station_service.domain.journalAudit.entity.JournalAudit;
import com.example.station_service.domain.journalAudit.mapper.JournalAuditMapper;
import com.example.station_service.domain.journalAudit.repository.JournalAuditRepository;
import com.example.station_service.domain.station.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor
public class JournalAuditServiceImpl implements JournalAuditService {
    private final JournalAuditRepository journalAuditRepository;
    private final StationRepository stationRepository;
    private final JournalAuditMapper mapper;
    @Override
    @Transactional(readOnly = true)
    public Page<JournalAuditDto> getAllJournal(Pageable pageable) {
        return journalAuditRepository.findAll(pageable)
                .map(mapper::toDto);
    }
    @Override
    @Transactional(readOnly = true)
    public Page<JournalAuditDto> getByStationAndPeriod(Long stationId, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        try {
            return journalAuditRepository.findByStationIdAndDateActionBetween(stationId, start, end, pageable)
                    .map(mapper::toDto);
        } catch (Exception e) {
            System.err.println("!!! ERROR in getByStationAndPeriod: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    @Override
    @Transactional(readOnly = true)
    public Page<JournalAuditDto> getByPeriod(LocalDateTime start, LocalDateTime end, Pageable pageable) {
        try {
            return journalAuditRepository.findByDateActionBetween(start, end, pageable)
                    .map(mapper::toDto);
        } catch (Exception e) {
            System.err.println("!!! ERROR in getByPeriod: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    @Override
    @Transactional(readOnly = true)
    public Page<JournalAuditDto> getByTypeActionAndPeriod(String typeAction, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        try {
            return journalAuditRepository.findByTypeActionAndDateActionBetween(typeAction, start, end, pageable)
                    .map(mapper::toDto);
        } catch (Exception e) {
            System.err.println("!!! ERROR in getByTypeActionAndPeriod: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    @Override
    @Transactional
    public void createJournal(JournalAuditDto dto) {
        try {
            System.out.println(">>> createJournal: type=" + dto.getTypeAction() + ", stationId=" + dto.getStationId());
            JournalAudit journalAudit = mapper.toEntity(dto);
            journalAudit.setStation(null);
            if (journalAudit.getDateAction() == null) {
                journalAudit.setDateAction(LocalDateTime.now());
            }
            if (dto.getStationId() != null) {
                stationRepository.findById(dto.getStationId()).ifPresent(journalAudit::setStation);
            }
            journalAuditRepository.save(journalAudit);
            System.out.println("<<< createJournal SUCCESS");
        } catch (Exception e) {
            System.err.println("!!! ERROR in createJournal: " + (e != null ? e.getMessage() : "null exception"));
            if (e != null) e.printStackTrace();
            throw e;
        }
    }
}