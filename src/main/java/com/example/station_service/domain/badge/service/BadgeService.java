package com.example.station_service.domain.badge.service;
import com.example.station_service.domain.badge.dto.BadgeSellRequest;
import com.example.station_service.domain.venteCarburant.dto.VenteCarburantDto;
public interface BadgeService {
    VenteCarburantDto sellFuelWithBadge(BadgeSellRequest request);
    String generateUniqueRFID();
    String assignNewBadge(Long clientId);
}