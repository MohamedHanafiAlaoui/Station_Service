package com.example.station_service.entity;

import com.example.station_service.entity.enums.TypeCarburant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private double capaciteMax;
    @Column(nullable = false)
    private double niveauActuel;
    @Column(nullable = false)
    private double prixParLitre;
    @Column(nullable = false)
    private Boolean enService = true;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;


    public boolean isDisponible()
    {
        return  Boolean.TRUE.equals(enService) && niveauActuel > 0;
    }
}
