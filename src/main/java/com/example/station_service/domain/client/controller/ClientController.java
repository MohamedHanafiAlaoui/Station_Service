package com.example.station_service.domain.client.controller;
import com.example.station_service.domain.client.dto.ClientDto;
import com.example.station_service.domain.client.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.math.BigDecimal;
@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYE')")
    public ResponseEntity<ClientDto> createClient(@RequestBody ClientDto dto) {
        return ResponseEntity.ok(clientService.createClient(dto));
    }
    @GetMapping("/{id}")
    @PreAuthorize(
            "hasAnyRole('ADMIN', 'EMPLOYE') or " +
                    "(hasRole('CLIENT') and @securityService.isClientOwner(authentication, #id))"
    )
    public ResponseEntity<ClientDto> getClientById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getClientById(id));
    }
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYE')")
    public ResponseEntity<List<ClientDto>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYE')")
    public ResponseEntity<List<ClientDto>> searchClients(@RequestParam String keyword) {
        return ResponseEntity.ok(clientService.searchClients(keyword));
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYE')")
    public ResponseEntity<ClientDto> updateClient(@PathVariable Long id, @RequestBody ClientDto dto) {
        return ResponseEntity.ok(clientService.updateClient(id, dto));
    }
    @PutMapping("/{id}/recharge")
    @PreAuthorize(
            "hasAnyRole('ADMIN', 'EMPLOYE') or " +
                    "(hasRole('CLIENT') and @securityService.isClientOwner(authentication, #id))"
    )
    public ResponseEntity<Void> rechargeSolde(@PathVariable Long id, @RequestParam BigDecimal montant) {
        clientService.rechargeSolde(id, montant);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYE')")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/{id}/restore")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYE')")
    public ResponseEntity<Void> restoreClient(@PathVariable Long id) {
        clientService.restoreClient(id);
        return ResponseEntity.ok().build();
    }
}