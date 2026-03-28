package com.example.station_service.infrastructure.security;
import com.example.station_service.domain.client.entity.Client;
import com.example.station_service.domain.client.repository.ClientRepository;
import com.example.station_service.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
@Slf4j
@Component("securityService")
@RequiredArgsConstructor
@Transactional(readOnly = true)

public class SecurityService {
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    public boolean isClientOwner(Authentication authentication, Long clientId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        String username = authentication.getName();
        Client client = clientRepository.findById(clientId)
                .orElse(null);
        if (client == null) {
            return false;
        }
        return username.equals(client.getUsername());
    }
    public boolean isUserOfStation(Authentication authentication, Long stationId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        String username = authentication.getName();
        log.debug(">>> Checking isUserOfStation: user={}, stationId={}", username, stationId);
        return userRepository.findByUsername(username)
                .map(user -> {
                    if (user instanceof com.example.station_service.domain.Employe.entity.Employe employe) {
                        boolean match = employe.getStation() != null && employe.getStation().getId() != null && employe.getStation().getId().equals(stationId);
                        log.debug("  > Employe station: {} | Match: {}", (employe.getStation() != null ? employe.getStation().getId() : "null"), match);
                        return match;
                    }
                    log.debug("  > User is not an Employe instance.");
                    return false;
                })

                .orElse(false);
    }
}