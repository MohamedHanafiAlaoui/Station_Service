package com.example.station_service.domain.rapport.entity;

import com.example.station_service.domain.Employe.entity.Employe;
import com.example.station_service.domain.rapport.entity.enums.TypeRapport;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "rapport")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rapport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private Date sateDebut;
    private Date dateFin;
    private TypeRapport type;
    private String donnees;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employe_id")
    private Employe employe;


}
