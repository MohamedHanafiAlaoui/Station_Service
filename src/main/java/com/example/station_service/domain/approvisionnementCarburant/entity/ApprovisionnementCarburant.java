package com.example.station_service.domain.approvisionnementCarburant.entity;

import com.example.station_service.domain.approvisionnementCarburant.entity.enums.TypeCarburant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "approvisionnementCarburant")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovisionnementCarburant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private Date dateApprovisionnement ;
    private Double quantite;
    private TypeCarburant typeCarburant;

}
