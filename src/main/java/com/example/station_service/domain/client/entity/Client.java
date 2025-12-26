package com.example.station_service.domain.client.entity;

import com.example.station_service.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "clients")
@PrimaryKeyJoinColumn(name = "user_id")
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@AllArgsConstructor
@SuperBuilder
public class Client extends User {

    @Column(unique = true)
    private String badgeRFID;
    private double solde=0.0;


}
