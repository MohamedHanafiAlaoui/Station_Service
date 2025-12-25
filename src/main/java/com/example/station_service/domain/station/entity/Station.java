package com.example.station_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "station")
@Data
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
}
