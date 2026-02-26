package com.example.station_service.domain.client.entity;

import com.example.station_service.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "clients")
@PrimaryKeyJoinColumn(name = "user_id")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@AllArgsConstructor
@SuperBuilder
public class Client extends User {

    @Column(unique = true)
    private String badgeRFID;

    @Enumerated(EnumType.STRING)
    private BadgeType badgeType;

    @Column(nullable = false)
    private BigDecimal solde = BigDecimal.ZERO;
}
