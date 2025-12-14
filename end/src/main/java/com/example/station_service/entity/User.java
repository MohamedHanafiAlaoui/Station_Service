package com.example.station_service.entity;

import com.example.station_service.entity.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, length = 50)
    private String username;
    @Column(nullable = false, length = 50)
    private String nom;
    @Column(nullable = false, length = 50)
    private String prenom;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation = new Date();
    @Column(nullable = false)
    private String password;
    private Boolean actif = true;
    @Enumerated(EnumType.STRING)
    private UserRole role;

}
