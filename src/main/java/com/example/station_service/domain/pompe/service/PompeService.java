package com.example.station_service.domain.pompe.service;

import com.example.station_service.domain.pompe.dto.PompeDto;

import java.util.List;

public interface PompeService {
    void createPompe(PompeDto dto);
    PompeDto getPompeById(Long id);

    List<PompeDto> getAllPompes();

    List<PompeDto> getActivePompes();

    List<PompeDto> getPompesByStation(Long stationId);

    List<PompeDto> searchPompes(String keyword);

    List<PompeDto> filterByNiveau(double niveau);

    PompeDto updatePompe(Long id, PompeDto dto);


    void deletePompe(Long id, boolean enService);

    PompeDto updatePompesell(Long id, double sell);


    PompeDto updatePompeAddNive(Long id, double nive);

    long countActivePompes();

}
