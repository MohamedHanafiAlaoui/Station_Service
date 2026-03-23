package com.example.station_service.domain.admin.entity;
import com.example.station_service.domain.user.entity.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;
@Entity
@DiscriminatorValue("ADMIN")
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class Admin extends User {
}