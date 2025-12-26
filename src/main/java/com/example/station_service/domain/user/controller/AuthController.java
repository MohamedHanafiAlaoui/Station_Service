package com.example.station_service.domain.user.controller;


import com.example.station_service.domain.user.dto.UserDto;
import com.example.station_service.domain.user.service.UserService;
import com.example.station_service.domain.user.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto request)
    {
        UserDto c = userService.registerUser(request);

        return  ResponseEntity.ok(c);
    }

    @PostMapping("/register/Employe")
    public ResponseEntity<UserDto> registerUserEmploye(@Valid @RequestBody UserDto request)
    {
        UserDto e = userService.registerUserEmploye(request);

        return  ResponseEntity.ok(e);
    }
}
