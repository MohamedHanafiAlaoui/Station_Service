package com.example.station_service.domain.journalAudit.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.time.LocalDateTime;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JournalAuditDto {
    private Long id;
    private LocalDateTime dateAction;
    @NotBlank
    @Size(max = 50)
    private String typeAction;
    @NotBlank
    @Size(max = 255)
    private String description;
    private Long stationId;
}