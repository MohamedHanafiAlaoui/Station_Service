package com.example.station_service.domain.user.repository;

import com.example.station_service.domain.user.dto.UserDto;
import com.example.station_service.domain.user.entity.User;
import com.example.station_service.domain.user.entity.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByRole(UserRole role);
}
