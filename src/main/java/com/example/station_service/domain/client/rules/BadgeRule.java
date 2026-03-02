package com.example.station_service.domain.client.rules;

import com.example.station_service.domain.client.entity.Client;
import java.math.BigDecimal;

public interface BadgeRule {
    void validate(Client client, BigDecimal quantite, BigDecimal montant);
}
