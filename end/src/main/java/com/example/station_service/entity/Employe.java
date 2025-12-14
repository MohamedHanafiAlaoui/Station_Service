package com.example.station_service.entity;

import com.example.station_service.entity.enums.RoleEmploye;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "employes")
@PrimaryKeyJoinColumn(name = "user_id")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Employe extends User{
    @Enumerated(EnumType.STRING)

    private RoleEmploye roleEmp;
}
