package com.example.station_service.domain.journalAudit.repository;
import com.example.station_service.domain.journalAudit.entity.JournalAudit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import org.springframework.data.repository.query.Param;
@Repository
public interface JournalAuditRepository extends JpaRepository<JournalAudit, Long> {
    @Query(value = "SELECT j FROM JournalAudit j WHERE j.station.id = :stationId AND j.dateAction BETWEEN :start AND :end",
           countQuery = "SELECT count(j.id) FROM JournalAudit j WHERE j.station.id = :stationId AND j.dateAction BETWEEN :start AND :end")
    Page<JournalAudit> findByStationIdAndDateActionBetween(
            @Param("stationId") Long stationId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable
    );
    @Query(value = "SELECT j FROM JournalAudit j WHERE j.dateAction BETWEEN :start AND :end",
           countQuery = "SELECT count(j.id) FROM JournalAudit j WHERE j.dateAction BETWEEN :start AND :end")
    Page<JournalAudit> findByDateActionBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable
    );
    @Query(value = "SELECT j FROM JournalAudit j WHERE j.typeAction = :typeAction AND j.dateAction BETWEEN :start AND :end",
           countQuery = "SELECT count(j.id) FROM JournalAudit j WHERE j.typeAction = :typeAction AND j.dateAction BETWEEN :start AND :end")
    Page<JournalAudit> findByTypeActionAndDateActionBetween(
            @Param("typeAction") String typeAction,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable
    );
}