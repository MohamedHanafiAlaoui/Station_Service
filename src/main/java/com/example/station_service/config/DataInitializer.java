package com.example.station_service.config;
import com.example.station_service.domain.Employe.entity.Employe;
import com.example.station_service.domain.Employe.repository.EmployeRepository;
import com.example.station_service.domain.admin.entity.Admin;
import com.example.station_service.domain.admin.repository.AdminRepository;
import com.example.station_service.domain.approvisionnementCarburant.entity.ApprovisionnementCarburant;
import com.example.station_service.domain.approvisionnementCarburant.entity.enums.TypeCarburant;
import com.example.station_service.domain.approvisionnementCarburant.repository.ApprovisionnementCarburantRepository;
import com.example.station_service.domain.client.entity.Client;
import com.example.station_service.domain.client.repository.ClientRepository;
import com.example.station_service.domain.journalAudit.entity.JournalAudit;
import com.example.station_service.domain.journalAudit.repository.JournalAuditRepository;
import com.example.station_service.domain.pompe.entity.Pompe;
import com.example.station_service.domain.pompe.repository.PompeRepository;
import com.example.station_service.domain.station.entity.Station;
import com.example.station_service.domain.station.repository.StationRepository;
import com.example.station_service.domain.user.entity.User;
import com.example.station_service.domain.user.entity.enums.UserRole;
import com.example.station_service.domain.user.repository.UserRepository;
import com.example.station_service.domain.venteCarburant.entity.StatutVente;
import com.example.station_service.domain.venteCarburant.entity.VenteCarburant;
import com.example.station_service.domain.venteCarburant.repository.VenteCarburantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final StationRepository stationRepository;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final EmployeRepository employeRepository;
    private final ClientRepository clientRepository;
    private final PompeRepository pompeRepository;
    private final VenteCarburantRepository venteCarburantRepository;
    private final ApprovisionnementCarburantRepository approvisionnementCarburantRepository;
    private final JournalAuditRepository journalAuditRepository;
    private final PasswordEncoder passwordEncoder;
    private final Random random = new Random();
    @Override
    @Transactional
    public void run(String... args) {
        try {
            if (userRepository.findByUsername("admin").isPresent()) {
                log.info("Admin user already exists. Skipping initialization.");
                return;
            }
            log.info("Admin user NOT found. Starting database initialization...");
        List<Station> stations = createStations();
        createAdminUser();
        List<Employe> employes = createEmployes(stations);
        List<Client> clients = createClients();
        List<Pompe> pompes = createPompes(stations);
        createVentesCarburant(pompes, clients, stations);
        createApprovisionnements(stations);
        createJournalAudit(stations);
        log.info("Database initialization completed successfully!");
        } catch (Exception e) {
            log.error("!!! ERROR during database initialization: {}", e.getMessage());
            e.printStackTrace();
        }
    }
    private boolean dataAlreadyExists() {
        return stationRepository.count() > 0 || userRepository.count() > 0;
    }
    private List<Station> createStations() {
        List<Station> stations = new ArrayList<>();
        Object[][] data = {
                {"Casablanca Maarif", 33.5883100, -7.6113800},
                {"Casablanca Sidi Maarouf", 33.5373000, -7.6400000},
                {"Casablanca Ain Sebaa", 33.6070000, -7.5300000},
                {"Casablanca Derb Sultan", 33.5800000, -7.6000000},
                {"Casablanca Hay Hassani", 33.5600000, -7.6700000},
                {"Rabat Agdal", 34.0132500, -6.8325500},
                {"Rabat Hassan", 34.0224000, -6.8229000},
                {"Rabat Yacoub Mansour", 34.0200000, -6.8500000},
                {"Rabat Souissi", 34.0100000, -6.8500000},
                {"Rabat Temara", 33.9300000, -6.9000000},
                {"Marrakech Gueliz", 31.6340000, -8.0089000},
                {"Marrakech Menara", 31.6167000, -8.0363000},
                {"Marrakech Massira", 31.6200000, -8.0500000},
                {"Marrakech Sidi Youssef", 31.6300000, -8.0200000},
                {"Marrakech Targa", 31.6500000, -8.0600000},
                {"Fes Centre", 34.0331000, -5.0003000},
                {"Fes Narjiss", 34.0200000, -5.0200000},
                {"Fes Saiss", 33.9800000, -4.9900000},
                {"Fes Zouagha", 34.0100000, -5.0100000},
                {"Fes Hay Riad", 34.0400000, -5.0000000},
                {"Tanger Centre", 35.7806000, -5.8136000},
                {"Tanger Malabata", 35.7800000, -5.7700000},
                {"Tanger Gzenaya", 35.7400000, -5.9000000},
                {"Tanger Beni Makada", 35.7600000, -5.8200000},
                {"Tanger Mesnana", 35.7700000, -5.8400000},
                {"Agadir Talborjt", 30.4278000, -9.5981000},
                {"Agadir Dakhla", 30.4000000, -9.5600000},
                {"Agadir Hay Mohammadi", 30.4200000, -9.5500000},
                {"Agadir Bensergao", 30.4100000, -9.5800000},
                {"Agadir Founty", 30.4200000, -9.6200000}
        };
        for (Object[] entry : data) {
            Station station = Station.builder()
                    .nom("Station " + entry[0])
                    .adresse(entry[0] + ", Maroc")
                    .latitude(new BigDecimal(entry[1].toString()))
                    .longitude(new BigDecimal(entry[2].toString()))
                    .active(true)
                    .build();
            stations.add(stationRepository.save(station));
        }
        log.info("Created {} stations", stations.size());
        return stations;
    }
    private void createAdminUser() {
        Admin admin = Admin.builder()
                .username("admin")
                .nom("Administrateur")
                .prenom("Système")
                .password(passwordEncoder.encode("Password123!"))
                .role(UserRole.ADMIN)
                .actif(true)
                .build();
        adminRepository.save(admin);
        log.info("Created admin user");
    }
    private List<Employe> createEmployes(List<Station> stations) {
        List<Employe> employes = new ArrayList<>();
        for (int i = 0; i < stations.size(); i++) {
            Station station = stations.get(i);
            Employe emp1 = Employe.builder()
                    .username("emp_" + (i + 1) + "_a")
                    .nom("Employe" + (i + 1))
                    .prenom("A")
                    .password(passwordEncoder.encode("Password123!"))
                    .role(UserRole.EMPLOYE)
                    .actif(true)
                    .station(station)
                    .build();
            Employe emp2 = Employe.builder()
                    .username("emp_" + (i + 1) + "_b")
                    .nom("Employe" + (i + 1))
                    .prenom("B")
                    .password(passwordEncoder.encode("Password123!"))
                    .role(UserRole.EMPLOYE)
                    .actif(true)
                    .station(station)
                    .build();
            employes.add(employeRepository.save(emp1));
            employes.add(employeRepository.save(emp2));
        }
        log.info("Created {} employees", employes.size());
        return employes;
    }
    private List<Client> createClients() {
        List<Client> clients = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            Client client = Client.builder()
                    .username("client_" + i)
                    .nom("ClientNom" + i)
                    .prenom("ClientPrenom" + i)
                    .password(passwordEncoder.encode("Password123!"))
                    .role(UserRole.CLIENT)
                    .actif(true)
                    .badgeRFID("RFID" + String.format("%04d", i))
                    .solde(BigDecimal.valueOf(500 + random.nextInt(5000)))
                    .build();
            clients.add(clientRepository.save(client));
        }
        log.info("Created {} clients", clients.size());
        return clients;
    }
    private List<Pompe> createPompes(List<Station> stations) {
        List<Pompe> pompes = new ArrayList<>();
        for (int i = 0; i < stations.size(); i++) {
            Station station = stations.get(i);
            for (int j = 1; j <= 3; j++) {
                Pompe pompe = Pompe.builder()
                        .codePompe("P" + (i + 1) + "-" + j)
                        .typeCarburant(j % 2 == 0 ? TypeCarburant.DIESEL : TypeCarburant.ESSENCE)
                        .capaciteMax(BigDecimal.valueOf(5000 + random.nextInt(3000)))
                        .niveauActuel(BigDecimal.valueOf(2000 + random.nextInt(2000)))
                        .prixParLitre(BigDecimal.valueOf(11 + random.nextDouble() * 3))
                        .enService(true)
                        .station(station)
                        .build();
                pompes.add(pompeRepository.save(pompe));
            }
        }
        log.info("Created {} pumps", pompes.size());
        return pompes;
    }
    private void createVentesCarburant(List<Pompe> pompes, List<Client> clients, List<Station> stations) {
        for (int i = 0; i < 100; i++) {
            Pompe pompe = pompes.get(random.nextInt(pompes.size()));
            Client client = clients.get(random.nextInt(clients.size()));
            BigDecimal quantite = BigDecimal.valueOf(10 + random.nextInt(40));
            BigDecimal prix = pompe.getPrixParLitre();
            BigDecimal montant = quantite.multiply(prix);
            VenteCarburant vente = VenteCarburant.builder()
                    .dateVente(LocalDateTime.now().minusDays(random.nextInt(60)))
                    .quantite(quantite)
                    .prixUnitaire(prix)
                    .montantPaye(montant)
                    .statut(random.nextBoolean() ? StatutVente.PAYE : StatutVente.ANNULE)
                    .pompe(pompe)
                    .client(client)
                    .station(pompe.getStation())
                    .build();
            venteCarburantRepository.save(vente);
        }
        log.info("Created 100 fuel sales");
    }
    private void createApprovisionnements(List<Station> stations) {
        for (int i = 0; i < 40; i++) {
            Station station = stations.get(random.nextInt(stations.size()));
            ApprovisionnementCarburant app = ApprovisionnementCarburant.builder()
                    .dateApprovisionnement(LocalDate.now().minusDays(random.nextInt(90)))
                    .quantite(1000 + random.nextInt(4000) + random.nextDouble()) 
                    .typeCarburant(random.nextBoolean() ? TypeCarburant.ESSENCE : TypeCarburant.DIESEL)
                    .station(station)
                    .build();
            approvisionnementCarburantRepository.save(app);
        }
        log.info("Created 40 fuel supplies");
    }
    private void createJournalAudit(List<Station> stations) {
        String[] actions = {"CREATION", "MODIFICATION", "SUPPRESSION", "VENTE", "APPROVISIONNEMENT", "CONNEXION", "DECONNEXION"};
        for (int i = 0; i < 50; i++) {
            Station station = stations.get(random.nextInt(stations.size()));
            JournalAudit audit = JournalAudit.builder()
                    .dateAction(LocalDateTime.now().minusDays(random.nextInt(120)))
                    .typeAction(actions[random.nextInt(actions.length)])
                    .description("Action enregistrée pour " + station.getNom())
                    .station(station)
                    .build();
            journalAuditRepository.save(audit);
        }
        log.info("Created 50 audit entries");
    }
}