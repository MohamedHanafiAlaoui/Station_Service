package com.example.station_service.domain.journalAudit.service;
import com.example.station_service.domain.journalAudit.dto.JournalAuditDto;
import com.example.station_service.domain.journalAudit.entity.JournalAudit;
import com.example.station_service.domain.journalAudit.mapper.JournalAuditMapper;
import com.example.station_service.domain.journalAudit.repository.JournalAuditRepository;
import com.example.station_service.domain.station.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
@Slf4j
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
            log.error("!!! ERROR in getByStationAndPeriod: {}", e.getMessage(), e);
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
            log.error("!!! ERROR in getByPeriod: {}", e.getMessage(), e);
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
            log.error("!!! ERROR in getByTypeActionAndPeriod: {}", e.getMessage(), e);
            throw e;
        }

    }
    @Override
    @Transactional
    public void createJournal(JournalAuditDto dto) {
        try {
            log.debug(">>> createJournal: type={}, stationId={}", dto.getTypeAction(), dto.getStationId());
            JournalAudit journalAudit = mapper.toEntity(dto);
            journalAudit.setStation(null);
            if (journalAudit.getDateAction() == null) {
                journalAudit.setDateAction(LocalDateTime.now());
            }
            if (dto.getStationId() != null) {
                stationRepository.findById(dto.getStationId()).ifPresent(journalAudit::setStation);
            }
            journalAuditRepository.save(journalAudit);
            log.debug("<<< createJournal SUCCESS");
        } catch (Exception e) {
            log.error("!!! ERROR in createJournal: {}", (e != null ? e.getMessage() : "null exception"), e);
            throw e;
        }

    }
}