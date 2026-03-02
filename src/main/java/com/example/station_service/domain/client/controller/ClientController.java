package com.example.station_service.domain.client.controller;

import com.example.station_service.domain.client.dto.ClientDto;
import com.example.station_service.domain.client.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    // ADMIN ONLY
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientDto> createClient(@RequestBody ClientDto dto) {
        return ResponseEntity.ok(clientService.createClient(dto));
    }

    // ADMIN + CLIENT (لكن Client يرى نفسه فقط)
    @GetMapping("/{id}")
    @PreAuthorize(
            "hasRole('ADMIN') or " +
                    "(hasRole('CLIENT') and @securityService.isClientOwner(authentication, #id))"
    )
    public ResponseEntity<ClientDto> getClientById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getClientById(id));
    }

    // ADMIN ONLY
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ClientDto>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    // ADMIN ONLY
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ClientDto>> searchClients(@RequestParam String keyword) {
        return ResponseEntity.ok(clientService.searchClients(keyword));
    }

    // ADMIN ONLY
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientDto> updateClient(@PathVariable Long id, @RequestBody ClientDto dto) {
        return ResponseEntity.ok(clientService.updateClient(id, dto));
    }

    // ADMIN + CLIENT (Client يشحن رصيده فقط)
    @PutMapping("/{id}/recharge")
    @PreAuthorize(
            "hasRole('ADMIN') or " +
                    "(hasRole('CLIENT') and @securityService.isClientOwner(authentication, #id))"
    )
    public ResponseEntity<Void> rechargeSolde(@PathVariable Long id, @RequestParam double montant) {
        clientService.rechargeSolde(id, montant);
        return ResponseEntity.ok().build();
    }

    // ADMIN ONLY
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}
