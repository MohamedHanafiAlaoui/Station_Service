package com.example.station_service.domain.client.service;

import com.example.station_service.domain.badge.service.BadgeService;
import com.example.station_service.domain.client.dto.ClientDto;
import com.example.station_service.domain.client.entity.Client;
import com.example.station_service.domain.client.mapper.ClientMapper;
import com.example.station_service.domain.client.repository.ClientRepository;
import com.example.station_service.domain.journalAudit.dto.JournalAuditDto;
import com.example.station_service.domain.journalAudit.service.JournalAuditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ClientServiceImpl Unit Tests")
class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private JournalAuditService journalAuditService;

    @Mock
    private BadgeService badgeService;

    @InjectMocks
    private ClientServiceImpl clientService;

    private Client client;
    private ClientDto clientDto;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1L);
        client.setNom("Alaoui");
        client.setPrenom("Mohamed");
        client.setUsername("m.alaoui");
        client.setBadgeRFID("RFID-12345");
        client.setSolde(BigDecimal.valueOf(200));
        client.setActif(true);

        clientDto = new ClientDto();
        clientDto.setId(1L);
        clientDto.setNom("Alaoui");
        clientDto.setPrenom("Mohamed");
        clientDto.setUsername("m.alaoui");
        clientDto.setBadgeRFID("RFID-12345");
        clientDto.setSolde(BigDecimal.valueOf(200));
        clientDto.setActif(true);
    }


    @Test
    @DisplayName("createClient - should create client with existing RFID")
    void createClient_shouldCreateClientWithRfid() {
        when(clientMapper.toEntity(clientDto)).thenReturn(client);
        when(clientRepository.save(client)).thenReturn(client);
        when(clientMapper.toDto(client)).thenReturn(clientDto);

        ClientDto result = clientService.createClient(clientDto);

        assertThat(result).isNotNull();
        assertThat(result.getNom()).isEqualTo("Alaoui");
        verify(clientRepository).save(client);
        verify(journalAuditService).createJournal(any(JournalAuditDto.class));
        verify(badgeService, never()).generateUniqueRFID();
    }

    @Test
    @DisplayName("createClient - should auto-generate RFID when not provided")
    void createClient_shouldAutoGenerateRfid() {
        clientDto.setBadgeRFID(null);
        when(badgeService.generateUniqueRFID()).thenReturn("AUTO-RFID-999");
        when(clientMapper.toEntity(clientDto)).thenReturn(client);
        when(clientRepository.save(client)).thenReturn(client);
        when(clientMapper.toDto(client)).thenReturn(clientDto);

        clientService.createClient(clientDto);

        verify(badgeService).generateUniqueRFID();
        assertThat(clientDto.getBadgeRFID()).isEqualTo("AUTO-RFID-999");
    }


    @Test
    @DisplayName("getClientById - should return DTO when client exists")
    void getClientById_shouldReturnDto() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientMapper.toDto(client)).thenReturn(clientDto);

        ClientDto result = clientService.getClientById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("getClientById - should throw when client not found")
    void getClientById_shouldThrowWhenNotFound() {
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clientService.getClientById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }


    @Test
    @DisplayName("updateClient - should update client fields")
    void updateClient_shouldUpdateFields() {
        clientDto.setNom("Updated");
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.save(client)).thenReturn(client);
        when(clientMapper.toDto(client)).thenReturn(clientDto);

        ClientDto result = clientService.updateClient(1L, clientDto);

        assertThat(client.getNom()).isEqualTo("Updated");
        verify(journalAuditService).createJournal(any(JournalAuditDto.class));
    }

    @Test
    @DisplayName("updateClient - should not overwrite RFID when new value is blank")
    void updateClient_shouldNotOverwriteRfidWhenBlank() {
        clientDto.setBadgeRFID("  ");
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.save(any())).thenReturn(client);
        when(clientMapper.toDto(any())).thenReturn(clientDto);

        clientService.updateClient(1L, clientDto);

        assertThat(client.getBadgeRFID()).isEqualTo("RFID-12345");
    }


    @Test
    @DisplayName("rechargeSolde - should add amount to existing solde")
    void rechargeSolde_shouldAddToSolde() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        clientService.rechargeSolde(1L, BigDecimal.valueOf(100));

        assertThat(client.getSolde()).isEqualByComparingTo(BigDecimal.valueOf(300));
        verify(clientRepository).save(client);
        verify(journalAuditService).createJournal(any(JournalAuditDto.class));
    }

    @Test
    @DisplayName("rechargeSolde - should handle null initial solde")
    void rechargeSolde_shouldHandleNullSolde() {
        client.setSolde(null);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        clientService.rechargeSolde(1L, BigDecimal.valueOf(50));

        assertThat(client.getSolde()).isEqualByComparingTo(BigDecimal.valueOf(50));
    }


    @Test
    @DisplayName("deleteClient - should soft delete and modify RFID")
    void deleteClient_shouldSoftDelete() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        clientService.deleteClient(1L);

        assertThat(client.getActif()).isFalse();
        assertThat(client.getBadgeRFID()).contains("_DEL_");
        verify(clientRepository).save(client);
        verify(journalAuditService).createJournal(any(JournalAuditDto.class));
    }


    @Test
    @DisplayName("restoreClient - should reactivate client and restore RFID")
    void restoreClient_shouldReactivate() {
        client.setActif(false);
        client.setBadgeRFID("RFID-12345_DEL_1711000000000");
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        clientService.restoreClient(1L);

        assertThat(client.getActif()).isTrue();
        assertThat(client.getBadgeRFID()).isEqualTo("RFID-12345");
        verify(journalAuditService).createJournal(any(JournalAuditDto.class));
    }


    @Test
    @DisplayName("getAllClients - should return list of all clients")
    void getAllClients_shouldReturnList() {
        when(clientRepository.findAll()).thenReturn(List.of(client));
        when(clientMapper.toDto(client)).thenReturn(clientDto);

        List<ClientDto> result = clientService.getAllClients();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("Alaoui");
    }


    @Test
    @DisplayName("searchClients - should return matching clients by nom")
    void searchClients_shouldMatchByNom() {
        when(clientRepository.findAll()).thenReturn(List.of(client));
        when(clientMapper.toDto(client)).thenReturn(clientDto);

        List<ClientDto> result = clientService.searchClients("alao");

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("searchClients - should return empty when no match")
    void searchClients_shouldReturnEmptyWhenNoMatch() {
        when(clientRepository.findAll()).thenReturn(List.of(client));

        List<ClientDto> result = clientService.searchClients("UNKNOWN_XYZ");

        assertThat(result).isEmpty();
    }
}
