package com.example.station_service.domain.approvisionnementCarburant.entity;
import com.example.station_service.domain.approvisionnementCarburant.entity.enums.TypeCarburant;
import com.example.station_service.domain.station.entity.Station;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
@Entity
@Table(name = "approvisionnementCarburant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovisionnementCarburant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private LocalDate dateApprovisionnement ;
    private Double quantite;
    private Double quantiteDisponible;
    private Double niveauAvant;
    private Double niveauApres;
    @Enumerated(EnumType.STRING)
    private TypeCarburant typeCarburant;
    @ManyToOne @JoinColumn(name = "station_id")
    private Station station;
}