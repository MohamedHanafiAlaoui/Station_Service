package com.example.station_service.domain.approvisionnementCarburant.service;

import com.example.station_service.domain.approvisionnementCarburant.dto.ApprovisionnementCarburantDto;
import com.example.station_service.domain.approvisionnementCarburant.entity.ApprovisionnementCarburant;
import com.example.station_service.domain.approvisionnementCarburant.mapper.ApprovisionnementCarburantMapper;
import com.example.station_service.domain.approvisionnementCarburant.repository.ApprovisionnementCarburantRepository;
import com.example.station_service.domain.journalAudit.dto.JournalAuditDto;
import com.example.station_service.domain.journalAudit.service.JournalAuditService;
import com.example.station_service.domain.station.entity.Station;
import com.example.station_service.domain.station.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApprovisionnementCarburantServiceImpl implements ApprovisionnementCarburantService {

    private final ApprovisionnementCarburantRepository repository;
    private final StationRepository stationRepository;
    private final ApprovisionnementCarburantMapper mapper;
    private final JournalAuditService journalAuditService;


    @Override
    public ApprovisionnementCarburantDto create(ApprovisionnementCarburantDto dto) {

        ApprovisionnementCarburant entity = mapper.toEntity(dto);

        Station station = stationRepository.findById(dto.getStationId())
                .orElseThrow(() -> new RuntimeException("Station not found"));
        entity.setStation(station);
        entity.setDateApprovisionnement(LocalDate.now());

        ApprovisionnementCarburant saved = repository.save(entity);

        JournalAuditDto audit = new JournalAuditDto();
        audit.setTypeAction("APPROVISIONNEMENT_CARBURANT");
        audit.setDescription(
                "Approvisionnement de " + saved.getQuantite() + " litres (" + saved.getTypeCarburant() + ")"
        );
        audit.setStationId(station.getId());

        journalAuditService.createJournal(audit);

        return mapper.toDto(saved);
    }


    @Override
    public List<ApprovisionnementCarburantDto> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public ApprovisionnementCarburantDto getById(Long id) {
        ApprovisionnementCarburant entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Approvisionnement not found"));

        return mapper.toDto(entity);
    }

    @Override
    public List<ApprovisionnementCarburantDto> getWeeklyReport() {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusWeeks(1);

        return repository.findByDateApprovisionnementBetween(start, end)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<ApprovisionnementCarburantDto> getMonthlyReport() {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusMonths(1);

        return repository.findByDateApprovisionnementBetween(start, end)
                .stream()
                .map(mapper::toDto)
                .toList();
    }
}
