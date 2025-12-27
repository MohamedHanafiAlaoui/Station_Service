package com.example.station_service.domain.client.repository;

import com.example.station_service.domain.client.entity.Client;
import com.example.station_service.domain.user.dto.UserDto;
import com.example.station_service.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {
    Optional<Client> findByBadgeRFID(String badgeRFID);
    Optional<Client> findByUsername(String username);
}
