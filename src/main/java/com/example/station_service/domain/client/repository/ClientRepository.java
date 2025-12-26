package com.example.station_service.domain.client.repository;

import com.example.station_service.domain.client.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {
    Optional<Client> findByBadgeRFID(String badgeRFID);

}
