package com.example.station_service.domain.venteCarburant.entity;

import com.example.station_service.domain.client.entity.Client;
import com.example.station_service.domain.pompe.entity.Pompe;
import com.example.station_service.domain.station.entity.Station;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "venteCarburant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VenteCarburant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id  ;
    private LocalDateTime dateVente;
    private BigDecimal montantPaye;
    private BigDecimal  quantite;
    private BigDecimal   prixUnitaire;
    @Enumerated(EnumType.STRING)

    private StatutVente   statut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pompe_id", nullable = false)
    private Pompe pompe;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @PrePersist
    public void prePersist() {
        if (this.dateVente == null) {
            this.dateVente = LocalDateTime.now();
        }
    }


}
