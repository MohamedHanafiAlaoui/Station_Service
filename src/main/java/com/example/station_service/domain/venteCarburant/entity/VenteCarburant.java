package com.example.station_service.entity;

import com.example.station_service.entity.enums.MondePaiment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "venteCarburant")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VenteCarburant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id  ;
    private Date dateVente;
    private Double  montantPaye;
    private Double quantite;
    private Double  prixUnitaire;
    private MondePaiment modePaiement;
    private String  statut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pompe_id", nullable = false)
    private Pompe pompe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employe_id")
    private Employe employe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;


}
