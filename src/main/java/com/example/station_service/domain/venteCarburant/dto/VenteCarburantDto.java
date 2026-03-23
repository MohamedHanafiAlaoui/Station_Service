package com.example.station_service.domain.venteCarburant.dto;
import com.example.station_service.domain.client.dto.ClientDto;
import com.example.station_service.domain.pompe.dto.PompeDto;
import com.example.station_service.domain.venteCarburant.entity.StatutVente;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VenteCarburantDto {
    private Long id;
    private LocalDateTime dateVente;
    private BigDecimal montantPaye;
    private BigDecimal montant; 
    private BigDecimal quantite;
    private BigDecimal prixUnitaire;
    private StatutVente statut;
    private PompeDto pompe;
    private ClientDto client;
    private Long pompeId;
    private Long clientId;
}