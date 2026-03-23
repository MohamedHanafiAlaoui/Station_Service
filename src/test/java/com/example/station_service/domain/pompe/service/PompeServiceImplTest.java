package com.example.station_service.domain.pompe.service;

import com.example.station_service.domain.approvisionnementCarburant.entity.enums.TypeCarburant;
import com.example.station_service.domain.journalAudit.dto.JournalAuditDto;
import com.example.station_service.domain.journalAudit.service.JournalAuditService;
import com.example.station_service.domain.pompe.dto.PompeDto;
import com.example.station_service.domain.pompe.entity.Pompe;
import com.example.station_service.domain.pompe.mapper.PompeMapper;
import com.example.station_service.domain.pompe.repository.PompeRepository;
import com.example.station_service.domain.station.entity.Station;
import com.example.station_service.domain.station.repository.StationRepository;
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
@DisplayName("PompeServiceImpl Unit Tests")
class PompeServiceImplTest {

    @Mock
    private PompeRepository pompeRepository;

    @Mock
    private PompeMapper pompeMapper;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private JournalAuditService journalAuditService;

    @InjectMocks
    private PompeServiceImpl pompeService;

    private Station station;
    private Pompe pompe;
    private PompeDto pompeDto;

    @BeforeEach
    void setUp() {
        station = Station.builder()
                .id(1L)
                .nom("Station Alpha")
                .build();

        pompe = Pompe.builder()
                .id(1L)
                .codePompe("PUMP-001")
                .typeCarburant(TypeCarburant.DIESEL)
                .capaciteMax(BigDecimal.valueOf(1000))
                .niveauActuel(BigDecimal.valueOf(500))
                .prixParLitre(BigDecimal.valueOf(10))
                .enService(true)
                .station(station)
                .build();

        pompeDto = new PompeDto();
        pompeDto.setId(1L);
        pompeDto.setCodePompe("PUMP-001");
        pompeDto.setTypeCarburant(TypeCarburant.DIESEL);
        pompeDto.setCapaciteMax(BigDecimal.valueOf(1000));
        pompeDto.setNiveauActuel(BigDecimal.valueOf(500));
        pompeDto.setPrixParLitre(BigDecimal.valueOf(10));
        pompeDto.setEnService(true);
        pompeDto.setStationId(1L);
    }

    // ─── createPompe ────────────────────────────────────────────────

    @Test
    @DisplayName("createPompe - should save pompe and log audit")
    void createPompe_shouldSaveAndReturnDto() {
        when(pompeRepository.findTopByOrderByIdDesc()).thenReturn(Optional.empty());
        when(pompeMapper.toEntity(pompeDto)).thenReturn(pompe);
        when(pompeRepository.save(pompe)).thenReturn(pompe);
        when(pompeMapper.toDto(pompe)).thenReturn(pompeDto);

        PompeDto result = pompeService.createPompe(pompeDto);

        assertThat(result).isNotNull();
        assertThat(result.getCodePompe()).isEqualTo("PUMP-001");
        verify(pompeRepository).save(any(Pompe.class));
        verify(journalAuditService).createJournal(any(JournalAuditDto.class));
    }

    @Test
    @DisplayName("createPompe - should generate sequential code PUMP-002 when one pompe already exists")
    void createPompe_shouldGenerateSequentialCode() {
        Pompe existingPompe = Pompe.builder().id(1L).codePompe("PUMP-001").station(station).build();
        when(pompeRepository.findTopByOrderByIdDesc()).thenReturn(Optional.of(existingPompe));
        when(pompeMapper.toEntity(any())).thenReturn(pompe);
        when(pompeRepository.save(any())).thenAnswer(inv -> {
            Pompe p = inv.getArgument(0);
            assertThat(p.getCodePompe()).isEqualTo("PUMP-002");
            return p;
        });
        when(pompeMapper.toDto(any())).thenReturn(pompeDto);

        pompeService.createPompe(pompeDto);

        verify(pompeRepository).save(argThat(p -> "PUMP-002".equals(p.getCodePompe())));
    }

    // ─── getPompeById ────────────────────────────────────────────────

    @Test
    @DisplayName("getPompeById - should return DTO when pompe exists")
    void getPompeById_shouldReturnDto() {
        when(pompeRepository.findById(1L)).thenReturn(Optional.of(pompe));
        when(pompeMapper.toDto(pompe)).thenReturn(pompeDto);

        PompeDto result = pompeService.getPompeById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("getPompeById - should throw when pompe not found")
    void getPompeById_shouldThrowWhenNotFound() {
        when(pompeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pompeService.getPompeById(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("99");
    }

    // ─── updatePompeAddNive ────────────────────────────────────────────────

    @Test
    @DisplayName("updatePompeAddNive - should add fuel to pompe")
    void updatePompeAddNive_shouldAddFuel() {
        when(pompeRepository.findById(1L)).thenReturn(Optional.of(pompe));
        when(pompeRepository.save(any())).thenReturn(pompe);
        when(pompeMapper.toDto(any())).thenReturn(pompeDto);

        PompeDto result = pompeService.updatePompeAddNive(1L, 100.0);

        assertThat(result).isNotNull();
        assertThat(pompe.getNiveauActuel()).isEqualByComparingTo(BigDecimal.valueOf(600));
        verify(pompeRepository).save(pompe);
    }

    @Test
    @DisplayName("updatePompeAddNive - should throw when capacity exceeded")
    void updatePompeAddNive_shouldThrowOnCapacityExceeded() {
        when(pompeRepository.findById(1L)).thenReturn(Optional.of(pompe));

        assertThatThrownBy(() -> pompeService.updatePompeAddNive(1L, 600.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("CAPACITY_EXCEEDED");
    }


    @Test
    @DisplayName("deletePompe - should deactivate pompe (soft delete)")
    void deletePompe_shouldDeactivate() {
        when(pompeRepository.findById(1L)).thenReturn(Optional.of(pompe));

        pompeService.deletePompe(1L, false);

        assertThat(pompe.getEnService()).isFalse();
        verify(pompeRepository).save(pompe);
        verify(journalAuditService).createJournal(any(JournalAuditDto.class));
    }


    @Test
    @DisplayName("getAllPompes - should return all pompes as DTOs")
    void getAllPompes_shouldReturnList() {
        when(pompeRepository.findAll()).thenReturn(List.of(pompe));
        when(pompeMapper.toDto(pompe)).thenReturn(pompeDto);

        List<PompeDto> result = pompeService.getAllPompes();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCodePompe()).isEqualTo("PUMP-001");
    }


    @Test
    @DisplayName("countActivePompes - should return number of active pompes")
    void countActivePompes_shouldReturnCount() {
        when(pompeRepository.findByEnServiceTrue()).thenReturn(List.of(pompe));

        long count = pompeService.countActivePompes();

        assertThat(count).isEqualTo(1L);
    }


    @Test
    @DisplayName("filterByNiveau - should return pompes above given niveau")
    void filterByNiveau_shouldFilterCorrectly() {
        when(pompeRepository.findAll()).thenReturn(List.of(pompe));
        when(pompeMapper.toDto(pompe)).thenReturn(pompeDto);

        List<PompeDto> result = pompeService.filterByNiveau(100.0);

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("filterByNiveau - should return empty when niveau too high")
    void filterByNiveau_shouldReturnEmpty() {
        when(pompeRepository.findAll()).thenReturn(List.of(pompe));

        List<PompeDto> result = pompeService.filterByNiveau(2000.0);

        assertThat(result).isEmpty();
    }
}
