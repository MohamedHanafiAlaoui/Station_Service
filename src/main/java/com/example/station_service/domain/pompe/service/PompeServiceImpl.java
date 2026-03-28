package com.example.station_service.domain.pompe.service;
import com.example.station_service.domain.approvisionnementCarburant.entity.ApprovisionnementCarburant;
import com.example.station_service.domain.approvisionnementCarburant.entity.enums.TypeCarburant;
import com.example.station_service.domain.approvisionnementCarburant.repository.ApprovisionnementCarburantRepository;
import com.example.station_service.domain.approvisionnementCarburant.service.ApprovisionnementCarburantService;
import com.example.station_service.domain.journalAudit.dto.JournalAuditDto;
import com.example.station_service.domain.journalAudit.service.JournalAuditService;
import com.example.station_service.domain.pompe.dto.PompeDto;
import com.example.station_service.domain.pompe.entity.Pompe;
import com.example.station_service.domain.pompe.mapper.PompeMapper;
import com.example.station_service.domain.pompe.repository.PompeRepository;
import com.example.station_service.domain.station.entity.Station;
import com.example.station_service.domain.station.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
@Slf4j
@Service("pompeService")
@RequiredArgsConstructor
public class PompeServiceImpl implements PompeService {

    private final PompeRepository pompeRepository;
    private final PompeMapper pompeMapper;
    private final StationRepository stationRepository;
    private final JournalAuditService journalAuditService;
    private final ApprovisionnementCarburantRepository approvisionnementCarburantRepository;
    private final ApprovisionnementCarburantService approvisionnementCarburantService;
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
    public Long getStationIdByPompe(Long id) {
        try {
            return pompeRepository.findById(id)
                    .map(p -> {
                        if (p.getStation() != null) {
                            return p.getStation().getId();
                        }
                        log.warn("!!! WARNING: Pompe {} has no station assigned.", id);
                        return null;
                    })
                    .orElse(null);
         } catch (Exception e) {
            log.error("!!! ERROR in getStationIdByPompe: {}", e.getMessage(), e);
            return null;
        }

    }

    @Override
    @Transactional
    public PompeDto ajouterCarburant(Long id, double quantity) {
        Pompe pompe = pompeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pompe non trouvée: " + id));

        if (!Boolean.TRUE.equals(pompe.getEnService())) {
            throw new IllegalStateException("La pompe " + pompe.getCodePompe() + " n'est pas en service.");
        }

        BigDecimal quantityBD = BigDecimal.valueOf(quantity);
        BigDecimal newNiveau = pompe.getNiveauActuel().add(quantityBD);
        if (newNiveau.compareTo(pompe.getCapaciteMax()) > 0) {
            throw new IllegalArgumentException("La quantité dépasse la capacité maximale de la pompe (" + pompe.getCapaciteMax() + " L).");
        }

        List<ApprovisionnementCarburant> supplies = approvisionnementCarburantRepository
                .findByStation_IdAndTypeCarburantAndQuantiteDisponibleGreaterThanOrderByDateApprovisionnementAsc(
                        pompe.getStation().getId(),
                        pompe.getTypeCarburant(),
                        0.0
                );

        if (supplies.isEmpty()) {
            throw new RuntimeException("Aucun stock disponible pour le carburant: " + pompe.getTypeCarburant());
        }

        double totalStock = supplies.stream()
                .mapToDouble(a -> a.getQuantiteDisponible() != null ? a.getQuantiteDisponible() : 0.0)
                .sum();
        
        if (totalStock < quantity) {
            throw new RuntimeException("Stock insuffisant. Disponible: " + String.format("%.2f", totalStock) + " L. Demandé: " + quantity + " L");
        }

        BigDecimal remainingToDeduct = quantityBD;
        for (ApprovisionnementCarburant supply : supplies) {
            if (remainingToDeduct.compareTo(BigDecimal.ZERO) <= 0) break;

            BigDecimal disponible = BigDecimal.valueOf(supply.getQuantiteDisponible() != null ? supply.getQuantiteDisponible() : 0.0);
            if (disponible.compareTo(BigDecimal.ZERO) <= 0) continue;

            if (disponible.compareTo(remainingToDeduct) >= 0) {
                supply.setQuantiteDisponible(disponible.subtract(remainingToDeduct).doubleValue());
                remainingToDeduct = BigDecimal.ZERO;
            } else {
                // Current supply is fully consumed
                remainingToDeduct = remainingToDeduct.subtract(disponible);
                supply.setQuantiteDisponible(0.0);
            }
            approvisionnementCarburantRepository.save(supply);
        }

        pompe.setNiveauActuel(newNiveau);
        Pompe updated = pompeRepository.save(pompe);

        JournalAuditDto audit = new JournalAuditDto();
        audit.setTypeAction("POMPE_RECHARGE");
        audit.setDescription("Remplissage pompe " + updated.getCodePompe() + " de " + quantity + " L (Déduction stock FIFO)");
        audit.setStationId(updated.getStation().getId());
        journalAuditService.createJournal(audit);

        return pompeMapper.toDto(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public double getAvailableStock(Long stationId, com.example.station_service.domain.approvisionnementCarburant.entity.enums.TypeCarburant type) {
        return approvisionnementCarburantRepository
                .findByStation_IdAndTypeCarburantAndQuantiteDisponibleGreaterThanOrderByDateApprovisionnementAsc(stationId, type, 0.0)
                .stream()
                .mapToDouble(a -> a.getQuantiteDisponible() != null ? a.getQuantiteDisponible() : 0.0)
                .sum();
    }





    @Override
    public     List<PompeDto> getPompsByType(TypeCarburant type)
    {
        return  pompeRepository.findAll()
                .stream().filter(a->a.getTypeCarburant().equals(type))
                .map(pompeMapper::toDto)
                .toList();
    }


}