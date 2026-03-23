package com.example.station_service.domain.Employe.entity;
import com.example.station_service.domain.station.entity.Station;
import com.example.station_service.domain.user.entity.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;
@Entity
@DiscriminatorValue("EMPLOYE")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
@SuperBuilder
public class Employe extends User {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = true)
    private Station station;
}