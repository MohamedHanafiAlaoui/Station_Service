package com.example.station_service.domain.station.entity;

import com.example.station_service.domain.Employe.entity.Employe;
import com.example.station_service.domain.pompe.entity.Pompe;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "station")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nom;
    private  String adresse;

    @Column(nullable = false)
    private boolean active = true;


    private LocalDateTime datecreation;


    @PrePersist
    protected void onCreate() {
        this.datecreation = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "station")
    private List<Pompe> pompes;

    @OneToMany(
            mappedBy = "station",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Employe> employes = new ArrayList<>();

}
