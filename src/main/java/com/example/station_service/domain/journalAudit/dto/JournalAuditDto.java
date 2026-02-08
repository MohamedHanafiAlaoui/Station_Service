package com.example.station_service.domain.journalAudit.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class JournalAuditDto {

    private Long id;

    @NotBlank
    @Size(max = 50)
    private String typeAction;

    @NotBlank
    @Size(max = 255)
    private String description;

    @NotNull
    private Long stationId;
}

