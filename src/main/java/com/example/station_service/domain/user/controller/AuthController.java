package com.example.station_service.domain.user.controller;
import com.example.station_service.domain.Employe.dto.EmployeDto;
import com.example.station_service.domain.Employe.entity.Employe;
import com.example.station_service.domain.user.dto.JwtResponse;
import com.example.station_service.domain.user.dto.LoginRequest;
import com.example.station_service.domain.user.dto.UserDto;
import com.example.station_service.domain.user.dto.Usernom;
import com.example.station_service.domain.user.service.UserService;
import com.example.station_service.infrastructure.security.JwtUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserDto request)
    {
        String c = userService.registerUser(request);
        return  ResponseEntity.ok(c);
    }
    @PostMapping("/register/Employe")
    public ResponseEntity<String > registerUserEmploye(@Valid @RequestBody UserDto request)
    {
        String e = userService.registerUserEmploye(request);
        return  ResponseEntity.ok(e);
    }
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest)
    {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findAny()
                .orElse("");
        Optional<UserDto> user = userService.findByUsername(userDetails.getUsername());
        Long id = user.map(UserDto::id).orElse(null);
        Long stationId = null;
        if (roles.equals("ROLE_EMPLOYE")) {
            Optional<EmployeDto> employe = userService.findEmployeByUserId(id);
            stationId = employe.map(EmployeDto::stationId).orElse(null);
        }
        return ResponseEntity.ok(
                new JwtResponse(jwt, userDetails.getUsername(), roles, id, stationId)
        );
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateNomPrenom(
            @PathVariable Long id,
            @RequestBody Usernom dto
    ) {
        return ResponseEntity.ok(userService.updateNomPrenom(id, dto));
    }
    @GetMapping("/employes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EmployeDto>> getAllEmployes() {
        return ResponseEntity.ok(userService.getAllEmployes());
    }
    @DeleteMapping("/employes/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEmploye(@PathVariable Long id) {
        userService.deleteEmploye(id);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/employes/{id}/restore")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> restoreEmploye(@PathVariable Long id) {
        userService.restoreEmploye(id);
        return ResponseEntity.ok().build();
    }
}