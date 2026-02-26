package com.example.station_service.domain.client.rules;

import com.example.station_service.domain.client.entity.Client;
import com.example.station_service.domain.venteCarburant.entity.VenteCarburant;

import java.math.BigDecimal;

public interface BadgeRule {
    void validate(Client client, VenteCarburant vente, BigDecimal montant);
}
