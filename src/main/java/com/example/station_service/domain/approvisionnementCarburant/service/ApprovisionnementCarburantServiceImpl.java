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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
@Slf4j
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
        
        // Calculate total available stock BEFORE this supply
        Double currentStock = repository.sumQuantiteDisponible(station.getId(), entity.getTypeCarburant());
        if (currentStock == null) currentStock = 0.0;
        
        entity.setStation(station);
        entity.setDateApprovisionnement(LocalDate.now());
        entity.setQuantiteDisponible(entity.getQuantite());
        entity.setNiveauAvant(currentStock);                // Snapshot Before
        entity.setNiveauApres(currentStock + entity.getQuantite()); // Snapshot After

        ApprovisionnementCarburant saved = repository.save(entity);
        JournalAuditDto audit = new JournalAuditDto();
        audit.setTypeAction("APPROVISIONNEMENT_CARBURANT");
        audit.setDescription(
                "Approvisionnement de " + saved.getQuantite() + " L (" + saved.getTypeCarburant() + "). Stock: " + saved.getNiveauAvant() + " -> " + saved.getNiveauApres() + " L"
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

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createMockStock(Long stationId, com.example.station_service.domain.approvisionnementCarburant.entity.enums.TypeCarburant type) {
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new RuntimeException("Station not found"));

        // Calculate snapshots for mock record
        Double currentStock = repository.sumQuantiteDisponible(stationId, type);
        if (currentStock == null) currentStock = 0.0;

        ApprovisionnementCarburant mock = new ApprovisionnementCarburant();
        mock.setQuantite(5000.0);
        mock.setQuantiteDisponible(5000.0);
        mock.setTypeCarburant(type);
        mock.setStation(station);
        mock.setDateApprovisionnement(LocalDate.now());
        mock.setNiveauAvant(currentStock);
        mock.setNiveauApres(currentStock + 5000.0);

        repository.save(mock);
        log.info(">>> MOCK STOCK CREATED: 5000L. Stock {} -> {} L", currentStock, mock.getNiveauApres());
    }
}