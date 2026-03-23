package com.example.station_service.domain.user.service;
import com.example.station_service.domain.Employe.dto.EmployeDto;
import com.example.station_service.domain.Employe.entity.Employe;
import com.example.station_service.domain.user.dto.UserDto;
import com.example.station_service.domain.user.dto.Usernom;
import java.util.List;
import java.util.Optional;
public interface  UserService {
    String registerUser(UserDto request);
    String registerUserEmploye(UserDto request);
     String updateNomPrenom(Long id, Usernom dto);
    Optional<UserDto> findByUsername(String username);
    Optional<EmployeDto> findEmployeByUserId(Long userId);
    List<EmployeDto> getAllEmployes();
    void deleteEmploye(Long id);
    void restoreEmploye(Long id);
}