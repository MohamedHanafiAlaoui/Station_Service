package com.example.station_service.domain.client.entity;
import com.example.station_service.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;
@Entity
@DiscriminatorValue("CLIENT")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@AllArgsConstructor
@SuperBuilder
public class Client extends User {
    @Column(unique = true)
    private String badgeRFID;
    private BigDecimal solde = BigDecimal.ZERO;
}