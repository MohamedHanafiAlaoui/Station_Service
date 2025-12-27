package com.example.station_service.domain.Employe.repository;

import com.example.station_service.domain.Employe.entity.Employe;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeRepository extends JpaRepository<Employe,Long> {
    Optional<Employe> findByUsername(String username);
}
