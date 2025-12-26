package com.example.station_service.domain.user.service;

import com.example.station_service.domain.user.dto.UserDto;

import java.util.Optional;

public interface  UserService {

    UserDto registerUser(UserDto request);

    UserDto registerUserEmploye(UserDto request);


    Optional<UserDto> findByUsername(String username);
}
