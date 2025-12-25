package com.example.station_service.entity;

import com.example.station_service.entity.enums.RoleEmploye;
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
public class Employe extends User{
    @Enumerated(EnumType.STRING)

    private RoleEmploye roleEmp;
}
