package com.example.station_service.domain.client.service;
import com.example.station_service.domain.badge.service.BadgeService;
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
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final JournalAuditService journalAuditService;
    private final BadgeService badgeService;
    @Override
    @org.springframework.transaction.annotation.Transactional
    public ClientDto createClient(ClientDto dto) {
        try {
            if (dto.getBadgeRFID() == null || dto.getBadgeRFID().trim().isEmpty()) {
                dto.setBadgeRFID(badgeService.generateUniqueRFID());
                System.out.println(">>> Generated automatic RFID for new client: " + dto.getBadgeRFID());
            }
            Client entity = clientMapper.toEntity(dto);
            Client saved = clientRepository.save(entity);
            JournalAuditDto audit = new JournalAuditDto();
            audit.setTypeAction("CREATE_CLIENT");
            audit.setDescription("Création du client: " + saved.getNom() + " " + saved.getPrenom() + " (RFID: " + saved.getBadgeRFID() + ")");
            audit.setStationId(null);
            journalAuditService.createJournal(audit);
            return clientMapper.toDto(saved);
        } catch (Exception e) {
            System.err.println("!!! ERROR in createClient: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    @Override
    @org.springframework.transaction.annotation.Transactional
    public ClientDto updateClient(Long id, ClientDto dto) {
        try {
            Client client = clientRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Client not found: " + id));
            client.setNom(dto.getNom());
            client.setPrenom(dto.getPrenom());
            if (dto.getBadgeRFID() != null && !dto.getBadgeRFID().trim().isEmpty()) {
                client.setBadgeRFID(dto.getBadgeRFID());
            }
            if (dto.getSolde() != null) {
                client.setSolde(dto.getSolde());
            }
            Client updated = clientRepository.save(client);
            JournalAuditDto audit = new JournalAuditDto();
            audit.setTypeAction("UPDATE_CLIENT");
            audit.setDescription("Mise à jour du client: " + updated.getNom() + " " + updated.getPrenom());
            audit.setStationId(null);
            journalAuditService.createJournal(audit);
            return clientMapper.toDto(updated);
        } catch (Exception e) {
            System.err.println("!!! ERROR in updateClient: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    @Override
    @org.springframework.transaction.annotation.Transactional
    public void rechargeSolde(Long id, BigDecimal montant) {
        try {
            Client client = clientRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Client not found: " + id));
            client.setSolde(
                    (client.getSolde() != null ? client.getSolde() : BigDecimal.ZERO).add(montant)
            );
            clientRepository.save(client);
            JournalAuditDto audit = new JournalAuditDto();
            audit.setTypeAction("RECHARGE_SOLDE");
            audit.setDescription("Recharge de " + montant + " DH pour le client: " + client.getNom());
            audit.setStationId(null);
            journalAuditService.createJournal(audit);
        } catch (Exception e) {
            System.err.println("!!! ERROR in rechargeSolde: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    @Override
    @org.springframework.transaction.annotation.Transactional
    public void deleteClient(Long id) {
        try {
            System.out.println(">>> Attempting soft-delete for client ID: " + id);
            Client client = clientRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Client not found: " + id));
            System.out.println(">>> Client found: " + client.getNom() + ". Setting actif=false");
            client.setActif(false);
            if (client.getBadgeRFID() != null) {
                String oldRfid = client.getBadgeRFID();
                client.setBadgeRFID(oldRfid + "_DEL_" + System.currentTimeMillis());
                System.out.println(">>> Updated RFID from " + oldRfid + " to " + client.getBadgeRFID());
            }
            clientRepository.save(client);
            System.out.println(">>> Client soft-deleted successfully in DB");
            JournalAuditDto audit = new JournalAuditDto();
            audit.setTypeAction("DELETE_CLIENT");
            audit.setDescription("Suppression (SOFT) du client: " + client.getNom() + " " + client.getPrenom());
            audit.setStationId(null);
            journalAuditService.createJournal(audit);
            System.out.println("<<< deleteClient SUCCESS");
        } catch (Exception e) {
            System.err.println("!!! ERROR in deleteClient: " + (e != null ? e.getMessage() : "null exception"));
            if (e != null) e.printStackTrace();
            throw e;
        }
    }
    @Override
    @org.springframework.transaction.annotation.Transactional
    public void restoreClient(Long id) {
        try {
            System.out.println(">>> Attempting to restore client ID: " + id);
            Client client = clientRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Client not found: " + id));
            client.setActif(true);
            if (client.getBadgeRFID() != null && client.getBadgeRFID().contains("_DEL_")) {
                String currentRfid = client.getBadgeRFID();
                String restoredRfid = currentRfid.split("_DEL_")[0];
                client.setBadgeRFID(restoredRfid);
                System.out.println(">>> Restored RFID from " + currentRfid + " to " + restoredRfid);
            }
            clientRepository.save(client);
            JournalAuditDto audit = new JournalAuditDto();
            audit.setTypeAction("RESTORE_CLIENT");
            audit.setDescription("Restauration du client: " + client.getNom() + " " + client.getPrenom());
            audit.setStationId(null);
            journalAuditService.createJournal(audit);
            System.out.println("<<< restoreClient SUCCESS");
        } catch (Exception e) {
            System.err.println("!!! ERROR in restoreClient: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
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
                        || (c.getBadgeRFID() != null && c.getBadgeRFID().toLowerCase().contains(keyword.toLowerCase())))
                .map(clientMapper::toDto)
                .toList();
    }
}