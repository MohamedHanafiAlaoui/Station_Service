package com.example.station_service.domain.venteCarburant.service;

import com.example.station_service.domain.client.entity.BadgeType;
import com.example.station_service.domain.client.entity.Client;
import com.example.station_service.domain.client.repository.ClientRepository;
import com.example.station_service.domain.client.rules.BadgeRuleFactory;
import com.example.station_service.domain.journalAudit.dto.JournalAuditDto;
import com.example.station_service.domain.journalAudit.service.JournalAuditService;
import com.example.station_service.domain.pompe.entity.Pompe;
import com.example.station_service.domain.pompe.repository.PompeRepository;
import com.example.station_service.domain.venteCarburant.dto.VenteCarburantDto;
import com.example.station_service.domain.venteCarburant.entity.VenteCarburant;
import com.example.station_service.domain.venteCarburant.mapper.VenteCarburantMapper;
import com.example.station_service.domain.venteCarburant.repository.VenteCarburantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VenteCarburantServiceImpl implements VenteCarburantService {

    private final VenteCarburantRepository venteCarburantRepository;
    private final VenteCarburantMapper mapper;
    private final PompeRepository pompeRepository;
    private final ClientRepository clientRepository;
    private final JournalAuditService journalAuditService;
    private final BadgeRuleFactory badgeRuleFactory;


    @Override
    public Page<VenteCarburantDto> getVentesByStationAndPeriod(Long stationId, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return venteCarburantRepository.findByStation_IdAndDateVenteBetween(stationId, start, end, pageable)
                .map(mapper::toDto);
    }

    @Override
    public Page<VenteCarburantDto> getVentesByStationAndPompeAndPeriod(Long stationId, Long pompeId, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return venteCarburantRepository.findByStation_IdAndPompe_IdAndDateVenteBetween(stationId, pompeId, start, end, pageable)
                .map(mapper::toDto);
    }

    @Override
    public Page<VenteCarburantDto> getVentesByClientAndPeriod(Long clientId, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return venteCarburantRepository.findByClientIdAndDateVenteBetween(clientId, start, end, pageable)
                .map(mapper::toDto);
    }

    @Override
    public BigDecimal getTotalQuantiteByStationAndPeriod(Long stationId, LocalDateTime start, LocalDateTime end) {
        return venteCarburantRepository.findByStation_IdAndDateVenteBetween(stationId, start, end, Pageable.unpaged())
                .stream()
                .map(VenteCarburant::getQuantite)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalMontantByStationAndPeriod(Long stationId, LocalDateTime start, LocalDateTime end) {
        return venteCarburantRepository.findByStation_IdAndDateVenteBetween(stationId, start, end, Pageable.unpaged())
                .stream()
                .map(VenteCarburant::getMontantPaye)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    @Override
    public VenteCarburantDto saveVente(VenteCarburantDto venteDTO) {

        VenteCarburant entity = mapper.toEntity(venteDTO);

        // --- Pompe ---
        if (venteDTO.getPompeId() == null) {
            throw new RuntimeException("PompeId is required");
        }
        Pompe pompe = pompeRepository.findById(venteDTO.getPompeId())
                .orElseThrow(() -> new RuntimeException("Pompe not found"));
        entity.setPompe(pompe);

        // --- Client ---
        if (venteDTO.getClientId() == null) {
            throw new RuntimeException("ClientId is required");
        }
        Client client = clientRepository.findById(venteDTO.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found"));
        entity.setClient(client);

        BigDecimal montant = pompe.getPrixParLitre().multiply(entity.getQuantite());

        badgeRuleFactory
                .getRule(client.getBadgeType())
                .validate(client, entity, montant);

        if (client.getBadgeType() == BadgeType.GOLD) {
            client.setSolde(client.getSolde().subtract(montant));
            clientRepository.save(client);
        }

        // --- Save vente ---
        VenteCarburant saved = venteCarburantRepository.save(entity);

        // --- Audit ---
        JournalAuditDto audit = new JournalAuditDto();
        audit.setTypeAction("VENTE_CARBURANT");
        audit.setDescription("Vente de " + saved.getQuantite() + " litres via pompe " + pompe.getCodePompe());
        audit.setStationId(pompe.getStation().getId());
        journalAuditService.createJournal(audit);

        return mapper.toDto(saved);
    }
}
