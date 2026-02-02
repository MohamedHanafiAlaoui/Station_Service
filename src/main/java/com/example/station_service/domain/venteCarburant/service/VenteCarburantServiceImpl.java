package com.example.station_service.domain.venteCarburant.service;

import com.example.station_service.domain.client.entity.Client;
import com.example.station_service.domain.pompe.entity.Pompe;
import com.example.station_service.domain.venteCarburant.dto.VenteCarburantDto;
import com.example.station_service.domain.venteCarburant.entity.VenteCarburant;
import com.example.station_service.domain.venteCarburant.mapper.VenteCarburantMapper;
import com.example.station_service.domain.venteCarburant.repository.VenteCarburantRepository;
import com.example.station_service.domain.client.repository.ClientRepository;
import com.example.station_service.domain.pompe.repository.PompeRepository;
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

        VenteCarburant saved = venteCarburantRepository.save(entity);
        return mapper.toDto(saved);
    }

}
