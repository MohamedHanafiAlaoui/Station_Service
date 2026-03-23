package com.example.station_service;

import com.example.station_service.config.DataInitializer;
import com.example.station_service.domain.station.repository.StationRepository;
import com.example.station_service.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class DataInitializerTest {

    @Autowired
    private DataInitializer dataInitializer;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads() {
        assertThat(dataInitializer).isNotNull();
        assertThat(stationRepository).isNotNull();
        assertThat(userRepository).isNotNull();
    }
}
