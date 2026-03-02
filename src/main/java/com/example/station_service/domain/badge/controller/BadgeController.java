package com.example.station_service.domain.badge.controller;

import com.example.station_service.domain.badge.dto.BadgeSellRequest;
import com.example.station_service.domain.badge.service.BadgeService;
import com.example.station_service.domain.venteCarburant.dto.VenteCarburantDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/badge")
@RequiredArgsConstructor
public class BadgeController {

    private final BadgeService badgeService;

    @PostMapping("/sell")
    public ResponseEntity<VenteCarburantDto> sellFuelWithBadge(@RequestBody BadgeSellRequest request) {
        return ResponseEntity.ok(badgeService.sellFuelWithBadge(request));
    }
}
