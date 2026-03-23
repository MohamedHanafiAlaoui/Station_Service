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
@Service("pompeService")
@RequiredArgsConstructor
public class PompeServiceImpl implements PompeService {
    private final PompeRepository pompeRepository;
    private final PompeMapper pompeMapper;
    private final StationRepository stationRepository;
    private final JournalAuditService journalAuditService;
    @Override
    public PompeDto createPompe(PompeDto dto) {
        long nextId = 1L;
        java.util.Optional<Pompe> lastPompe = pompeRepository.findTopByOrderByIdDesc();
        if (lastPompe.isPresent()) {
            nextId = lastPompe.get().getId() + 1;
        }
        String generatedCode = String.format("PUMP-%03d", nextId);
        Pompe pompe = pompeMapper.toEntity(dto);
        pompe.setCodePompe(generatedCode); 
        Pompe saved = pompeRepository.save(pompe);
        JournalAuditDto audit = new JournalAuditDto();
        audit.setTypeAction("CREATE_POMPE");
        audit.setDescription("Création de la pompe: " + saved.getCodePompe());
        audit.setStationId(saved.getStation() != null ? saved.getStation().getId() : null);
        journalAuditService.createJournal(audit);
        return pompeMapper.toDto(saved);
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
    @org.springframework.transaction.annotation.Transactional
    public PompeDto updatePompeAddNive(Long id, double nive) {
        try {
            System.out.println(">>> updatePompeAddNive: id=" + id + ", quantity=" + nive);
            Pompe pompe = pompeRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Pompe not found: " + id));
            BigDecimal niveBD = BigDecimal.valueOf(nive);
            BigDecimal currentLevel = pompe.getNiveauActuel() != null ? pompe.getNiveauActuel() : BigDecimal.ZERO;
            BigDecimal maxCapacity = pompe.getCapaciteMax() != null ? pompe.getCapaciteMax() : BigDecimal.valueOf(Double.MAX_VALUE);
            if (maxCapacity.compareTo(currentLevel.add(niveBD)) < 0) {
                String errorMsg = "CAPACITY_EXCEEDED: La quantité (" + niveBD + " L) dépasse la capacité maximale (" + maxCapacity + " L). Niveau actuel: " + currentLevel + " L.";
                System.err.println("!!! " + errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }
            pompe.setNiveauActuel(currentLevel.add(niveBD));
            Pompe updated = pompeRepository.save(pompe);
            try {
                JournalAuditDto audit = new JournalAuditDto();
                audit.setTypeAction("POMPE_ADD_LEVEL");
                audit.setDescription("Ajout de " + nive + " litres à la pompe " + pompe.getCodePompe() + " (Nouveau niveau: " + pompe.getNiveauActuel() + ")");
                if (pompe.getStation() != null && pompe.getStation().getId() != null) {
                    audit.setStationId(pompe.getStation().getId());
                } else {
                    audit.setStationId(null);
                }
                journalAuditService.createJournal(audit);
            } catch (Exception auditEx) {
                System.err.println("!!! WARNING: Failed to create audit log for pompe add level: " + auditEx.getMessage());
            }
            System.out.println("<<< updatePompeAddNive SUCCESS");
            return pompeMapper.toDto(updated);
        } catch (IllegalArgumentException e) {
            System.err.println("!!! VALIDATION ERROR in updatePompeAddNive: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("!!! UNEXPECTED ERROR in updatePompeAddNive: " + e.getClass().getName() + " - " + e.getMessage());
            e.printStackTrace();
            throw e; 
        }
    }
    @Override
    public Long getStationIdByPompe(Long id) {
        try {
            return pompeRepository.findById(id)
                    .map(p -> {
                        if (p.getStation() != null) {
                            return p.getStation().getId();
                        }
                        System.err.println("!!! WARNING: Pompe " + id + " has no station assigned.");
                        return null;
                    })
                    .orElse(null);
        } catch (Exception e) {
            System.err.println("!!! ERROR in getStationIdByPompe: " + e.getMessage());
            return null;
        }
    }
}