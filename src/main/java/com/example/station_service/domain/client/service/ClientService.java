package com.example.station_service.domain.client.service;

import com.example.station_service.domain.client.dto.ClientDto;

import java.util.List;

public interface ClientService {


    ClientDto createClient(ClientDto dto);

    ClientDto getClientById(Long id);
    List<ClientDto> getAllClients();
    List<ClientDto> searchClients(String keyword);

    ClientDto updateClient(Long id, ClientDto dto);


    void rechargeSolde(Long id, double montant);

    void deleteClient(Long id);
}
