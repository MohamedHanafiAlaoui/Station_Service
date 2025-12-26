package com.example.station_service.domain.Employe.repository;

import com.example.station_service.domain.Employe.entity.Employe;
import com.example.station_service.domain.Employe.entity.enums.RoleEmploye;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeRepository extends JpaRepository<Employe,Long> {
    List<Employe> findByRoleEmp(RoleEmploye roleEmp);
}
