package com.example.station_service.entity;

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

public class JournalAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date dateAction ;
    private String typeAction ;
    private String Description ;

    @ManyToOne
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

}
