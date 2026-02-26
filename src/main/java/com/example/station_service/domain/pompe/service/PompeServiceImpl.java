package com.example.station_service.domain.pompe.service;

import com.example.station_service.domain.journalAudit.dto.JournalAuditDto;
import com.example.station_service.domain.journalAudit.service.JournalAuditService;
import com.example.station_service.domain.pompe.dto.PompeDto;
import com.example.station_service.domain.pompe.entity.Pompe;
import com.example.station_service.domain.pompe.mapper.PompeMapper;
import com.example.station_service.domain.pompe.repository.PompeRepository;
import com.example.station_service.domain.station.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PompeServiceImpl implements PompeService {

    private final PompeRepository pompeRepository;
    private final PompeMapper pompeMapper;
    private final StationRepository stationRepository;
    private final JournalAuditService journalAuditService;

    @Override
    public void createPompe(PompeDto dto) {
        Pompe pompe = pompeMapper.toEntity(dto);
        Pompe saved = pompeRepository.save(pompe);

        JournalAuditDto audit = new JournalAuditDto();
        audit.setTypeAction("CREATE_POMPE");
        audit.setDescription("Création de la pompe: " + saved.getCodePompe());
        audit.setStationId(saved.getStation().getId());
        journalAuditService.createJournal(audit);
    }

    @Override
    public PompeDto getPompeById(Long id) {
        Pompe pompe = pompeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pompe not found: " + id));
        return pompeMapper.toDto(pompe);
    }

    @Override
    public List<PompeDto> getAllPompes() {
        return pompeRepository.findAll()
                .stream()
                .map(pompeMapper::toDto)
                .toList();
    }

    @Override
    public List<PompeDto> getActivePompes() {
        return pompeRepository.findByEnServiceTrue()
                .stream()
                .map(pompeMapper::toDto)
                .toList();
    }

    @Override
    public List<PompeDto> getPompesByStation(Long stationId) {
        return pompeRepository.findByStation_Id(stationId)
                .stream()
                .map(pompeMapper::toDto)
                .toList();
    }

    @Override
    public List<PompeDto> searchPompes(String keyword) {
        return pompeRepository.findAll().stream()
                .filter(p -> p.getCodePompe().toLowerCase().contains(keyword.toLowerCase()))
                .map(pompeMapper::toDto)
                .toList();
    }

    @Override
    public PompeDto updatePompe(Long id, PompeDto dto) {
        Pompe pompe = pompeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pompe not found: " + id));

        pompe.setCodePompe(dto.getCodePompe());
        pompe.setTypeCarburant(dto.getTypeCarburant());
        pompe.setCapaciteMax(dto.getCapaciteMax());
        pompe.setNiveauActuel(dto.getNiveauActuel());
        pompe.setPrixParLitre(dto.getPrixParLitre());
        pompe.setEnService(dto.getEnService());

        if (dto.getStationId() != null) {
            var station = stationRepository.findById(dto.getStationId())
                    .orElseThrow(() -> new IllegalArgumentException("Station not found: " + dto.getStationId()));
            pompe.setStation(station);
        }

        Pompe updated = pompeRepository.save(pompe);

        JournalAuditDto audit = new JournalAuditDto();
        audit.setTypeAction("UPDATE_POMPE");
        audit.setDescription("Mise à jour de la pompe: " + updated.getCodePompe());
        audit.setStationId(updated.getStation().getId());
        journalAuditService.createJournal(audit);

        return pompeMapper.toDto(updated);
    }

    @Override
    public List<PompeDto> filterByNiveau(double niveau) {
        BigDecimal niv = BigDecimal.valueOf(niveau);

        return pompeRepository.findAll().stream()
                .filter(p -> p.getNiveauActuel().compareTo(niv) > 0)
                .map(pompeMapper::toDto)
                .toList();
    }

    @Override
    public void deletePompe(Long id, boolean enService) {
        Pompe pompe = pompeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pompe not found: " + id));

        pompe.setEnService(enService);
        pompeRepository.save(pompe);

        JournalAuditDto audit = new JournalAuditDto();
        audit.setTypeAction(enService ? "ACTIVATE_POMPE" : "DEACTIVATE_POMPE");
        audit.setDescription((enService ? "Activation" : "Désactivation") + " de la pompe: " + pompe.getCodePompe());
        audit.setStationId(pompe.getStation().getId());
        journalAuditService.createJournal(audit);
    }

    @Override
    public long countActivePompes() {
        return pompeRepository.findByEnServiceTrue().size();
    }

    @Override
    public PompeDto updatePompesell(Long id, double sele) {
        Pompe pompe = pompeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pompe not found: " + id));

        BigDecimal seleBD = BigDecimal.valueOf(sele);

        if (pompe.getNiveauActuel().compareTo(seleBD) < 0) {
            throw new IllegalArgumentException("Pompe " + id + " has insufficient level: " + pompe.getNiveauActuel());
        }

        pompe.setNiveauActuel(pompe.getNiveauActuel().subtract(seleBD));
        Pompe updated = pompeRepository.save(pompe);

        JournalAuditDto audit = new JournalAuditDto();
        audit.setTypeAction("POMPE_SELL");
        audit.setDescription("Retrait de " + sele + " litres de la pompe " + pompe.getCodePompe());
        audit.setStationId(pompe.getStation().getId());
        journalAuditService.createJournal(audit);

        return pompeMapper.toDto(updated);
    }

    @Override
    public PompeDto updatePompeAddNive(Long id, double nive) {
        Pompe pompe = pompeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pompe not found: " + id));

        BigDecimal niveBD = BigDecimal.valueOf(nive);

        if (pompe.getCapaciteMax().compareTo(pompe.getNiveauActuel().add(niveBD)) < 0) {
            throw new IllegalArgumentException("Pompe " + id + " cannot exceed maximum capacity: " + pompe.getCapaciteMax());
        }

        pompe.setNiveauActuel(pompe.getNiveauActuel().add(niveBD));
        Pompe updated = pompeRepository.save(pompe);

        JournalAuditDto audit = new JournalAuditDto();
        audit.setTypeAction("POMPE_ADD_LEVEL");
        audit.setDescription("Ajout de " + nive + " litres à la pompe " + pompe.getCodePompe());
        audit.setStationId(pompe.getStation().getId());
        journalAuditService.createJournal(audit);

        return pompeMapper.toDto(updated);
    }
}
