package com.example.station_service.domain.Employe.entity;

import com.example.station_service.domain.Employe.entity.enums.RoleEmploye;
import com.example.station_service.domain.station.entity.Station;
import com.example.station_service.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "employes")
@PrimaryKeyJoinColumn(name = "user_id")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
@SuperBuilder
public class Employe extends User {
    @Enumerated(EnumType.STRING)

    private RoleEmploye roleEmp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;
}
