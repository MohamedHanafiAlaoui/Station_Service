package com.example.station_service.domain.client.rules;

import com.example.station_service.domain.client.entity.Client;
import com.example.station_service.domain.venteCarburant.entity.VenteCarburant;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class GoldBadgeRule implements BadgeRule {

    @Override
    public void validate(Client client, VenteCarburant vente, BigDecimal montant) {

        if (client.getSolde().compareTo(montant) < 0) {
            throw new RuntimeException("Solde insuffisant pour un client GOLD");
        }
    }
}
