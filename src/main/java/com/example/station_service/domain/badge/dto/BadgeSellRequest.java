package com.example.station_service.domain.badge.dto;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class BadgeSellRequest {
    private String badgeCode;
    private double quantity;
    private Long pompeId;
}