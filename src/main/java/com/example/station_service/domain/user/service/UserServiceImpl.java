package com.example.station_service.domain.user.service;
import com.example.station_service.domain.Employe.dto.EmployeDto;
import com.example.station_service.domain.Employe.entity.Employe;
import com.example.station_service.domain.Employe.repository.EmployeRepository;
import com.example.station_service.domain.client.entity.Client;
import com.example.station_service.domain.client.repository.ClientRepository;
import com.example.station_service.domain.journalAudit.dto.JournalAuditDto;
import com.example.station_service.domain.journalAudit.service.JournalAuditService;
import com.example.station_service.domain.station.entity.Station;
import com.example.station_service.domain.station.repository.StationRepository;
import com.example.station_service.domain.user.dto.UserDto;
import com.example.station_service.domain.user.dto.Usernom;
import com.example.station_service.domain.user.entity.User;
import com.example.station_service.domain.user.entity.enums.UserRole;
import com.example.station_service.domain.user.mapper.UserMapper;
import com.example.station_service.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements  UserService  {
    private  final UserRepository userRepository;
    private final EmployeRepository employeRepository;
    private final ClientRepository clientRepository;
    private  final StationRepository stationRepository;
    private final PasswordEncoder passwordEncoder;
    private  final UserMapper mapper;
    private  final JournalAuditService journalAuditService;
    @Override
    public String registerUser(UserDto request)
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
                .solde(BigDecimal.ZERO)
                .badgeRFID(null)
                .build();
        clientRepository.save(client);
        return "Client registered successfully";
    }
    @Override
    public String updateNomPrenom(Long id, Usernom dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    return new RuntimeException("User not found: " + id);
                });
        user.setNom(dto.getNom());
        user.setPrenom(dto.getPrenom());
        userRepository.save(user);
        return "Nom et prénom mis à jour avec succès";
    }
    @Override
   public String registerUserEmploye(UserDto request)
   {
       Station station = null;
       if (request.role() == UserRole.ADMIN || request.role() == UserRole.CLIENT)
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
               .role(request.role())
               .prenom(request.prenom())
               .password(passwordEncoder.encode(request.password()))
               .station(station)
               .build();
       employeRepository.save(employe);
       return "Employe registered successfully";
   }
    @Override
    public Optional<UserDto> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(mapper::toDto);
    }
    @Override
    public Optional<EmployeDto> findEmployeByUserId(Long userId) {
        return employeRepository.findById(userId)
                .map(emp -> new EmployeDto(
                        emp.getId(),
                        emp.getStation() != null ? emp.getStation().getId() : null,
                        emp.getNom(),
                        emp.getPrenom(),
                        emp.getActif()
                ));
    }
    @Override
    @Transactional(readOnly = true)
    public List<EmployeDto> getAllEmployes() {
        return employeRepository.findAll().stream()
                .map(emp -> new EmployeDto(
                        emp.getId(),
                        emp.getStation() != null ? emp.getStation().getId() : null,
                        emp.getNom(),
                        emp.getPrenom(),
                        emp.getActif()
                ))
                .collect(Collectors.toList());
    }
    @Override
    @Transactional
    public void deleteEmploye(Long id) {
        log.info(">>> Soft-deleting employee: {}", id);
        Employe employe = employeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        employe.setActif(false);
        employeRepository.save(employe);
        JournalAuditDto audit = new JournalAuditDto();
        audit.setTypeAction("DELETE_EMPLOYE");
        audit.setDescription("Désactivation de l'employé: " + employe.getNom() + " " + employe.getPrenom());
        audit.setStationId(employe.getStation() != null ? employe.getStation().getId() : null);
        journalAuditService.createJournal(audit);
    }
    @Override
    @Transactional
    public void restoreEmploye(Long id) {
        log.info(">>> Restoring employee: {}", id);
        Employe employe = employeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        employe.setActif(true);
        employeRepository.save(employe);
        JournalAuditDto audit = new JournalAuditDto();
        audit.setTypeAction("RESTORE_EMPLOYE");
        audit.setDescription("Réactivation de l'employé: " + employe.getNom() + " " + employe.getPrenom());
        audit.setStationId(employe.getStation() != null ? employe.getStation().getId() : null);
        journalAuditService.createJournal(audit);
    }
}