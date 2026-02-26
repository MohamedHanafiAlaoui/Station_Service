package com.example.station_service.domain.client.service;

import com.example.station_service.domain.client.dto.ClientDto;
import com.example.station_service.domain.client.entity.Client;
import com.example.station_service.domain.client.mapper.ClientMapper;
import com.example.station_service.domain.client.repository.ClientRepository;
import com.example.station_service.domain.journalAudit.dto.JournalAuditDto;
import com.example.station_service.domain.journalAudit.service.JournalAuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final JournalAuditService journalAuditService;

    @Override
    public ClientDto createClient(ClientDto dto) {

        Client entity = clientMapper.toEntity(dto);
        Client saved = clientRepository.save(entity);

        JournalAuditDto audit = new JournalAuditDto();
        audit.setTypeAction("CREATE_CLIENT");
        audit.setDescription("Création du client: " + saved.getNom() + " " + saved.getPrenom());
        audit.setStationId(null);

        journalAuditService.createJournal(audit);

        return clientMapper.toDto(saved);
    }

    @Override
    public ClientDto updateClient(Long id, ClientDto dto) {

        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found: " + id));

        client.setNom(dto.getNom());
        client.setPrenom(dto.getPrenom());
        client.setBadgeRFID(dto.getBadgeRFID());
        client.setSolde(dto.getSolde());

        Client updated = clientRepository.save(client);

        JournalAuditDto audit = new JournalAuditDto();
        audit.setTypeAction("UPDATE_CLIENT");
        audit.setDescription("Mise à jour du client: " + updated.getNom() + " " + updated.getPrenom());
        audit.setStationId(null);

        journalAuditService.createJournal(audit);

        return clientMapper.toDto(updated);
    }

    @Override
    public void rechargeSolde(Long id, double montant) {

        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found: " + id));

        client.setSolde(
                client.getSolde().add(BigDecimal.valueOf(montant))
        );

        clientRepository.save(client);

        JournalAuditDto audit = new JournalAuditDto();
        audit.setTypeAction("RECHARGE_SOLDE");
        audit.setDescription("Recharge de " + montant + " DH pour le client: " + client.getNom());
        audit.setStationId(null);

        journalAuditService.createJournal(audit);
    }

    @Override
    public void deleteClient(Long id) {

        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found: " + id));

        clientRepository.delete(client);

        JournalAuditDto audit = new JournalAuditDto();
        audit.setTypeAction("DELETE_CLIENT");
        audit.setDescription("Suppression du client: " + client.getNom() + " " + client.getPrenom());
        audit.setStationId(null);

        journalAuditService.createJournal(audit);
    }

    @Override
    public ClientDto getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found: " + id));
        return clientMapper.toDto(client);
    }

    @Override
    public List<ClientDto> getAllClients() {
        return clientRepository.findAll()
                .stream()
                .map(clientMapper::toDto)
                .toList();
    }

    @Override
    public List<ClientDto> searchClients(String keyword) {
        return clientRepository.findAll().stream()
                .filter(c -> c.getNom().toLowerCase().contains(keyword.toLowerCase())
                        || c.getPrenom().toLowerCase().contains(keyword.toLowerCase())
                        || c.getBadgeRFID().toLowerCase().contains(keyword.toLowerCase()))
                .map(clientMapper::toDto)
                .toList();
    }
}
