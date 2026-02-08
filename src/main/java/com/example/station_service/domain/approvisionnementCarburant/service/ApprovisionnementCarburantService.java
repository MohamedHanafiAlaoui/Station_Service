package com.example.station_service.domain.approvisionnementCarburant.service;

import com.example.station_service.domain.approvisionnementCarburant.dto.ApprovisionnementCarburantDto;

import java.util.List;

public interface ApprovisionnementCarburantService {

    ApprovisionnementCarburantDto create(ApprovisionnementCarburantDto dto);

    List<ApprovisionnementCarburantDto> getAll();

    ApprovisionnementCarburantDto getById(Long id);

    List<ApprovisionnementCarburantDto> getWeeklyReport();

    List<ApprovisionnementCarburantDto> getMonthlyReport();
}
