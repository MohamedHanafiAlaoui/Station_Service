package com.example.station_service.dto;

import com.example.station_service.entity.enums.RoleEmploye;
import com.example.station_service.validation.RegexPatterns;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class EmployeDto {
    private Long id;
    @NotBlank
    @Pattern(regexp = RegexPatterns.USERNAME)
    private String nom;
    @NotBlank
    @Pattern(regexp = RegexPatterns.USERNAME)
    private String prenom;
    @NotNull
    private RoleEmploye roleEmp;
}