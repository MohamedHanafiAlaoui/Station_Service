package com.example.station_service.domain.station.service;
import com.example.station_service.domain.journalAudit.dto.JournalAuditDto;
import com.example.station_service.domain.journalAudit.service.JournalAuditService;
import com.example.station_service.domain.pompe.entity.Pompe;
import com.example.station_service.domain.pompe.repository.PompeRepository;
import com.example.station_service.domain.station.dto.StationDto;
import com.example.station_service.domain.station.dto.StationPublicDto;
import com.example.station_service.domain.station.entity.Station;
import com.example.station_service.domain.station.mapper.StationMapper;
import com.example.station_service.domain.station.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
@Slf4j
@Service
@RequiredArgsConstructor
public class StationServiceImpl implements StationService {

    private final StationRepository stationRepository;
    private final PompeRepository pompeRepository;
    private final StationMapper stationMapper;
    private final JournalAuditService journalAuditService;
    @Override
    public void createStation(StationDto dto) {
        Station entity = stationMapper.toEntity(dto);
        Station saved = stationRepository.save(entity);
        JournalAuditDto audit = new JournalAuditDto();
        audit.setTypeAction("CREATE_STATION");
        audit.setDescription("Création de la station: " + saved.getNom());
        audit.setStationId(saved.getId());
        journalAuditService.createJournal(audit);
    }
    @Override
    public StationDto getStationById(Long id) {
        Station station = stationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Station not found: " + id));
        return stationMapper.toDto(station);
    }
    @Override
    public List<StationDto> getAllStations() {
        return stationRepository.findAll()
                .stream()
                .map(stationMapper::toDto)
                .toList();
    }
    @Override
    public List<StationDto> getActiveStations() {
        return stationRepository.findByActiveTrue()
                .stream()
                .map(stationMapper::toDto)
                .toList();
    }
    @Override
    public List<StationDto> getInactiveStations() {
        return stationRepository.findByActiveFalse()
                .stream()
                .map(stationMapper::toDto)
                .toList();
    }
    @Override
    public long countActiveStations() {
        return stationRepository.countByActiveTrue();
    }
    @Override
    public long countInactiveStations() {
        return stationRepository.countByActiveFalse();
    }
    @Override
    public List<StationDto> searchStations(String keyword) {
        return stationRepository.findByNomContainingIgnoreCase(keyword)
                .stream()
                .map(stationMapper::toDto)
                .toList();
    }
    @Override
    public List<StationDto> searchActiveStations(String keyword) {
        return stationRepository.findByNomContainingIgnoreCaseAndActiveTrue(keyword)
                .stream()
                .map(stationMapper::toDto)
                .toList();
    }
    @Override
    public StationDto updateStation(Long id, StationDto dto) {
        Station station = stationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Station not found: " + id));
        station.setNom(dto.getNom());
        station.setActive(dto.isActive());
        station.setLatitude(dto.getLatitude());
        station.setLongitude(dto.getLongitude());
        Station updated = stationRepository.save(station);
        JournalAuditDto audit = new JournalAuditDto();
        audit.setTypeAction("UPDATE_STATION");
        audit.setDescription("Mise à jour de la station: " + updated.getNom());
        audit.setStationId(updated.getId());
        journalAuditService.createJournal(audit);
        return stationMapper.toDto(updated);
    }
    @Override
    @Transactional
    public void deleteStation(Long id, boolean active) {
        Station station = stationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Station not found: " + id));
        station.setActive(active);
        stationRepository.save(station);
        List<Pompe> pompes = pompeRepository.findByStation_Id(id);
        if (pompes != null && !pompes.isEmpty()) {
            for (Pompe p : pompes) {
                p.setEnService(active);
            }
            pompeRepository.saveAll(pompes);
            log.info(">>> Cascaded ACTIVE={} to {} pumps for station: {}", active, pompes.size(), station.getNom());
        }
        JournalAuditDto audit = new JournalAuditDto();
        audit.setTypeAction(active ? "ACTIVATE_STATION" : "DEACTIVATE_STATION");
        audit.setDescription((active ? "Activation" : "Désactivation") + " de la station: " + station.getNom() + 
                           (active ? " (Pompes réactivées)" : " (Pompes désactivées)"));
        audit.setStationId(station.getId());
        journalAuditService.createJournal(audit);
    }
    @Override
    public List<StationPublicDto> getAllStationsPublic() {
        return stationRepository.findAll()
                .stream()
                .map(s -> {
                    StationPublicDto dto = new StationPublicDto();
                    dto.setId(s.getId());
                    dto.setNom(s.getNom());
                    dto.setAdresse(s.getAdresse());
                    dto.setLatitude(s.getLatitude());
                    dto.setLongitude(s.getLongitude());
                    return dto;
                })
                .toList();
    }
}