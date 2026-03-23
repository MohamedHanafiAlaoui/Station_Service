package com.example.station_service.domain.venteCarburant.service;
import com.example.station_service.domain.client.repository.ClientRepository;
import com.example.station_service.domain.client.entity.Client;
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
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
public class VenteCarburantServiceImpl implements VenteCarburantService {
    private final VenteCarburantRepository venteCarburantRepository;
    private final VenteCarburantMapper mapper;
    private final PompeRepository pompeRepository;
    private final ClientRepository clientRepository;
    private final JournalAuditService journalAuditService;
    @Override
    @Transactional(readOnly = true)
    public Page<VenteCarburantDto> getVentesByStationAndPeriod(Long stationId, LocalDate start, LocalDate end, Pageable pageable) {
        try {
            LocalDateTime startDT = start.atStartOfDay();
            LocalDateTime endDT = end.atTime(LocalTime.MAX);
            return venteCarburantRepository.findByStationIdAndDateVenteBetween(stationId, startDT, endDT, pageable)
                    .map(mapper::toDto);
        } catch (Exception e) {
            System.err.println("!!! ERROR in getVentesByStationAndPeriod: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    @Override
    @Transactional(readOnly = true)
    public Page<VenteCarburantDto> getVentesByStationAndPompeAndPeriod(Long stationId, Long pompeId, LocalDate start, LocalDate end, Pageable pageable) {
        try {
            LocalDateTime startDT = start.atStartOfDay();
            LocalDateTime endDT = end.atTime(LocalTime.MAX);
            return venteCarburantRepository.findByStationIdAndPompeIdAndDateVenteBetween(stationId, pompeId, startDT, endDT, pageable)
                    .map(mapper::toDto);
        } catch (Exception e) {
            System.err.println("!!! ERROR in getVentesByStationAndPompeAndPeriod: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    @Override
    @Transactional(readOnly = true)
    public Page<VenteCarburantDto> getVentesByClientAndPeriod(Long clientId, LocalDate start, LocalDate end, Pageable pageable) {
        LocalDateTime startDT = start.atStartOfDay();
        LocalDateTime endDT = end.atTime(LocalTime.MAX);
        return venteCarburantRepository.findByClientIdAndDateVenteBetween(clientId, startDT, endDT, pageable)
                .map(mapper::toDto);
    }
    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalQuantiteByStationAndPeriod(Long stationId, LocalDate start, LocalDate end) {
        LocalDateTime startDT = start.atStartOfDay();
        LocalDateTime endDT = end.atTime(LocalTime.MAX);
        BigDecimal total = venteCarburantRepository.sumQuantiteByStationAndDateVenteBetween(stationId, startDT, endDT);
        log.info(">>> getTotalQuantite for station {}: range {} to {} -> Result: {}", stationId, startDT, endDT, total);
        return total != null ? total : BigDecimal.ZERO;
    }
    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalMontantByStationAndPeriod(Long stationId, LocalDate start, LocalDate end) {
        LocalDateTime startDT = start.atStartOfDay();
        LocalDateTime endDT = end.atTime(LocalTime.MAX);
        BigDecimal total = venteCarburantRepository.sumMontantByStationAndDateVenteBetween(stationId, startDT, endDT);
        log.info(">>> getTotalMontant for station {}: range {} to {} -> Result: {}", stationId, startDT, endDT, total);
        return total != null ? total : BigDecimal.ZERO;
    }
    @Override
    @Transactional
    public VenteCarburantDto saveVente(VenteCarburantDto venteDTO) {
        VenteCarburant entity = mapper.toEntity(venteDTO);
        if (venteDTO.getPompeId() == null) {
            throw new RuntimeException("PompeId is required");
        }
        Pompe pompe = pompeRepository.findById(venteDTO.getPompeId())
                .orElseThrow(() -> new RuntimeException("Pompe not found"));
        entity.setPompe(pompe);
        if (venteDTO.getClientId() == null) {
            throw new RuntimeException("ClientId is required");
        }
        Client client = clientRepository.findById(venteDTO.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found"));
        entity.setClient(client);
        BigDecimal montant = pompe.getPrixParLitre().multiply(entity.getQuantite());
        VenteCarburant saved = venteCarburantRepository.save(entity);
        JournalAuditDto audit = new JournalAuditDto();
        audit.setTypeAction("VENTE_CARBURANT");
        audit.setDescription("Vente de " + saved.getQuantite() + " litres via pompe " + pompe.getCodePompe() + " (Client: " + client.getNom() + ")");
        audit.setStationId(pompe.getStation().getId());
        try {
            journalAuditService.createJournal(audit);
        } catch (Exception auditEx) {
            System.err.println("!!! WARNING: Failed to create audit log for vente: " + auditEx.getMessage());
        }
        return mapper.toDto(saved);
    }
    @Override
    @Transactional(readOnly = true)
    public Page<VenteCarburantDto> getAllVentes(Pageable pageable) {
        return venteCarburantRepository.findAll(pageable)
                .map(mapper::toDto);
    }
}