package com.example.station_service.domain.station.service;

import com.example.station_service.domain.journalAudit.dto.JournalAuditDto;
import com.example.station_service.domain.journalAudit.service.JournalAuditService;
import com.example.station_service.domain.station.dto.StationDto;
import com.example.station_service.domain.station.entity.Station;
import com.example.station_service.domain.station.mapper.StationMapper;
import com.example.station_service.domain.station.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StationServiceImpl implements   StationService{


    private final StationRepository stationRepository;
    private final StationMapper stationMapper;
    private final JournalAuditService journalAuditService;


    @Override
    public  void createStation(StationDto dto)
    {
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
        Station station =stationRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("not found " + id));

        return stationMapper.toDto(station);
    }

    @Override
    public List<StationDto> getAllStations()
    {
        return stationRepository.findAll()
                .stream()
                .map(stationMapper::toDto)
                .toList();
    }


    @Override
    public  List<StationDto>  getActiveStations()
    {
        return  stationRepository.findByActiveTrue()
                .stream()
                .map(stationMapper::toDto)
                .toList();

    }

    @Override
    public  List<StationDto> getInactiveStations()
    {
        return stationRepository.findByActiveFalse()
                .stream()
                .map(stationMapper::toDto)
                .toList();
    }


    @Override
    public long countActiveStations()
    {
        return  stationRepository.countByActiveTrue();
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
        Station updated = stationRepository.save(station);
        JournalAuditDto audit = new JournalAuditDto();
        audit.setTypeAction("UPDATE_STATION");
        audit.setDescription("Mise à jour de la station: " + updated.getNom());
        audit.setStationId(updated.getId()); journalAuditService.createJournal(audit);
        return stationMapper.toDto(updated);

    }


    @Override
    public void deleteStation(Long id, boolean active) {
        Station station = stationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Station not found: " + id));
        station.setActive(active); stationRepository.save(station);
        JournalAuditDto audit = new JournalAuditDto();
        audit.setTypeAction(active ? "ACTIVATE_STATION" : "DEACTIVATE_STATION");
        audit.setDescription((active ? "Activation" : "Désactivation") + " de la station: " + station.getNom());
        audit.setStationId(station.getId());
        journalAuditService.createJournal(audit);
    }


}
