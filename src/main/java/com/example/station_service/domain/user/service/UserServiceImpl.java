package com.example.station_service.domain.user.service;

import com.example.station_service.domain.Employe.entity.Employe;
import com.example.station_service.domain.Employe.repository.EmployeRepository;
import com.example.station_service.domain.client.entity.Client;
import com.example.station_service.domain.client.repository.ClientRepository;
import com.example.station_service.domain.station.entity.Station;
import com.example.station_service.domain.station.repository.StationRepository;
import com.example.station_service.domain.user.dto.UserDto;
import com.example.station_service.domain.user.entity.enums.UserRole;
import com.example.station_service.domain.user.mapper.UserMapper;
import com.example.station_service.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements  UserService  {

    private  final UserRepository userRepository;
    private final EmployeRepository employeRepository;
    private final ClientRepository clientRepository;
    private  final StationRepository stationRepository;
    private final PasswordEncoder passwordEncoder;
    private  final UserMapper mapper;



    @Override
    public UserDto registerUser(UserDto request)
    {
        if (request.role() != UserRole.CLIENT)
        {
            throw new IllegalArgumentException(" Role must be CLIENT for this method");
        }

        Client client=Client.builder()
                .nom(request.nom())
                .username(request.username())
                .actif(true)
                .role(UserRole.CLIENT)
                .prenom(request.prenom())
                .password(passwordEncoder.encode(request.password()))
                .build();
        clientRepository.save(client);
        return mapper.toDto(client) ;

    };

    @Override
   public UserDto registerUserEmploye(UserDto request)
   {
       Station station = null;
       if (request.role() != UserRole.EMPLOYE)
       {
           throw new   IllegalArgumentException("Role must be EMPLOYE  for this method");
       }

       if (request.stationId() != null) {
            station = stationRepository.findById(request.stationId())
                    .orElseThrow( () -> new  IllegalArgumentException( "Station not found"));
       }
       Employe employe = Employe.builder()
               .nom(request.nom())
               .username(request.username())
               .actif(true)
               .role(UserRole.EMPLOYE)
               .prenom(request.prenom())
               .password(passwordEncoder.encode(request.password()))
               .roleEmp(request.roleEmploye())
               .station(station)

               .build();

       employeRepository.save(employe);


       return mapper.toDto(employe);
   };

    @Override
    public Optional<UserDto> findByUsername(String username)
    {
    return null;
    }
}
