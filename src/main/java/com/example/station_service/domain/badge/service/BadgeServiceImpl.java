package com.example.station_service.domain.badge.service;

import com.example.station_service.domain.badge.dto.BadgeSellRequest;
import com.example.station_service.domain.client.entity.Client;
import com.example.station_service.domain.client.repository.ClientRepository;
import com.example.station_service.domain.client.rules.BadgeRuleFactory;
import com.example.station_service.domain.journalAudit.dto.JournalAuditDto;
import com.example.station_service.domain.journalAudit.service.JournalAuditService;
import com.example.station_service.domain.pompe.entity.Pompe;
import com.example.station_service.domain.pompe.repository.PompeRepository;
import com.example.station_service.domain.venteCarburant.dto.VenteCarburantDto;
import com.example.station_service.domain.venteCarburant.entity.StatutVente;
import com.example.station_service.domain.venteCarburant.entity.VenteCarburant;
import com.example.station_service.domain.venteCarburant.mapper.VenteCarburantMapper;
import com.example.station_service.domain.venteCarburant.repository.VenteCarburantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BadgeServiceImpl implements BadgeService {

    private final ClientRepository clientRepository;
    private final PompeRepository pompeRepository;
    private final VenteCarburantRepository venteRepository;
    private final VenteCarburantMapper venteMapper;
    private final JournalAuditService journalAuditService;
    private final BadgeRuleFactory badgeRuleFactory;

    @Override
    public VenteCarburantDto sellFuelWithBadge(BadgeSellRequest request) {

        Client client = clientRepository.findByBadgeRFID(request.getBadgeCode())
                .orElseThrow(() -> new RuntimeException("Badge invalide"));

        BigDecimal quantity = BigDecimal.valueOf(request.getQuantity());

        Pompe pompe = pompeRepository.findById(request.getPompeId())
                .orElseThrow(() -> new RuntimeException("Pompe introuvable"));

        if (!pompe.getTypeCarburant().equals(client.getBadgeType())) {
            throw new RuntimeException("Type carburant incompatible");
        }

        if (pompe.getNiveauActuel().compareTo(quantity) < 0) {
            throw new RuntimeException("Quantité insuffisante dans la pompe");
        }

        BigDecimal prixUnitaire = pompe.getPrixParLitre();
        BigDecimal montantPaye = prixUnitaire.multiply(quantity);

        badgeRuleFactory
                .getRule(client.getBadgeType())
                .validate(client, quantity, montantPaye);

        client.setSolde(client.getSolde().subtract(montantPaye));
        clientRepository.save(client);

        pompe.setNiveauActuel(pompe.getNiveauActuel().subtract(quantity));
        pompeRepository.save(pompe);

        VenteCarburant vente = new VenteCarburant();
        vente.setClient(client);
        vente.setPompe(pompe);
        vente.setStation(pompe.getStation());
        vente.setQuantite(quantity);
        vente.setPrixUnitaire(prixUnitaire);
        vente.setMontantPaye(montantPaye);
        vente.setStatut(StatutVente.PAYE);
        vente.setDateVente(LocalDateTime.now());

        VenteCarburant saved = venteRepository.save(vente);

        JournalAuditDto audit = new JournalAuditDto();
        audit.setTypeAction("VENTE_BADGE");
        audit.setDescription("Vente via badge: " + quantity + " litres");
        audit.setStationId(pompe.getStation().getId());
        journalAuditService.createJournal(audit);

        return venteMapper.toDto(saved);
    }
}
