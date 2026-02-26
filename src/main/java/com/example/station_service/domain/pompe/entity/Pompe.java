package com.example.station_service.domain.pompe.entity;

import com.example.station_service.domain.approvisionnementCarburant.entity.enums.TypeCarburant;
import com.example.station_service.domain.station.entity.Station;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "pompe")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pompe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codePompe;

    @Enumerated(EnumType.STRING)
    private TypeCarburant typeCarburant;

    @Column(nullable = false)
    private BigDecimal capaciteMax;

    @Column(nullable = false)
    private BigDecimal niveauActuel;

    @Column(nullable = false)
    private BigDecimal prixParLitre;

    @Column(nullable = false)
    private Boolean enService = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    public boolean isDisponible() {
        return Boolean.TRUE.equals(enService) && niveauActuel.compareTo(BigDecimal.ZERO) > 0;
    }
}
