package com.example.station_service.domain.client.rules;

import com.example.station_service.domain.client.entity.Client;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class NormalBadgeRule implements BadgeRule {

    private static final double LIMIT = 50.0;

    @Override
    public void validate(Client client, BigDecimal quantite, BigDecimal montant) {

        if (quantite.doubleValue() > LIMIT) {
            throw new RuntimeException(
                    "Client NORMAL ne peut pas dépasser " + LIMIT + " litres par transaction"
            );
        }
    }
}
