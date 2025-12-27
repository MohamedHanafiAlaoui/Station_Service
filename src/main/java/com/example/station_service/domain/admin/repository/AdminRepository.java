package com.example.station_service.domain.admin.repository;

import com.example.station_service.domain.admin.entity.Admin;
import com.example.station_service.domain.user.dto.UserDto;
import com.example.station_service.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Long> {
    Optional<Admin> findByUsername(String username);
}
